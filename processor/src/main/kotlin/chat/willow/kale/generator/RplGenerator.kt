package chat.willow.kale.generator

import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

annotation class SourceTargetContent(val numeric: String)
annotation class SourceTargetChannelContent(val numeric: String)

class RplGenerator : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf("*")
    }

    fun rpl_container(numeric: String, name: String, parent: ClassName, parameters: Iterable<ParameterSpec>): TypeSpec {
        val message = ClassName.bestGuess("$parent.Message")
        val parser = ClassName.bestGuess("$parent.Parser")
        val serialiser = ClassName.bestGuess("$parent.Serialiser")
        val descriptor = ClassName.bestGuess("$parent.Descriptor")
        val command = ClassName.bestGuess("ICommand")

        val messageTypeSpec = TypeSpec
                .classBuilder("Message")
                .superclass(message)
                .primaryConstructor(
                        FunSpec
                                .constructorBuilder()
                                .addParameters(parameters)
                                .build()
                )
                .addSuperclassConstructorParameter(CodeBlock.of(parameters.map { it.name }.joinToString(separator = ", ")))
                .build()

        val parserTypeSpec = TypeSpec
                .objectBuilder("Parser")
                .superclass(parser)
                .addSuperclassConstructorParameter(CodeBlock.of("command"))
                .build()

        val serialiserTypeSpec = TypeSpec
                .objectBuilder(serialiser)
                .superclass(serialiser)
                .addSuperclassConstructorParameter(CodeBlock.of("command"))
                .build()

        val descriptorTypeSpec = TypeSpec
                .objectBuilder(descriptor)
                .superclass(descriptor)
                .addSuperclassConstructorParameter(CodeBlock.of("command, Parser"))
                .build()

        return TypeSpec
                .objectBuilder("RPL_${name.toUpperCase()}")
                .addSuperinterface(command)
                .addProperty(PropertySpec
                        .builder("command", String::class, KModifier.OVERRIDE)
                        .initializer(CodeBlock.of("%S", numeric))
                        .build()
                )
                .addType(messageTypeSpec)
                .addType(parserTypeSpec)
                .addType(serialiserTypeSpec)
                .addType(descriptorTypeSpec)
                .build()
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver()) {
            return true
        }

        val kaleNumericsTypeSpec = TypeSpec.objectBuilder("KaleNumerics")

        generateSourceTargetContent(kaleNumericsTypeSpec, roundEnv)
        generateSourceTargetChannelContent(kaleNumericsTypeSpec, roundEnv)

        val kaleNumerics = kaleNumericsTypeSpec.build()

        val fileSpec = FileSpec.builder("chat.willow.kale.generated", "KaleNumerics")
        fileSpec.addType(kaleNumerics)
        fileSpec.addStaticImport("chat.willow.kale.core", "*")

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        fileSpec.build().writeTo(File(kaptKotlinGeneratedDir, ""))

        return true
    }

    fun generateSourceTargetContent(kaleNumericsTypeSpec: TypeSpec.Builder, roundEnv: RoundEnvironment) {
        val sourceTargetContentsToGenerate = roundEnv.getElementsAnnotatedWith(SourceTargetContent::class.java)

        sourceTargetContentsToGenerate
                .forEach {
                    val annotation = it.getAnnotation(SourceTargetContent::class.java)!!
                    val numeric = annotation.numeric
                    val className = it.simpleName.toString()

                    val parent = ClassName.bestGuess("RplSourceTargetContent")
                    val parameters = listOf(
                            ParameterSpec.builder("source", String::class).build(),
                            ParameterSpec.builder("target", String::class).build(),
                            ParameterSpec.builder("content", String::class).build()
                    )

                    generate(kaleNumericsTypeSpec, numeric, className, parent, parameters)
                }
    }

    fun generateSourceTargetChannelContent(kaleNumericsTypeSpec: TypeSpec.Builder, roundEnv: RoundEnvironment) {
        val sourceTargetContentsToGenerate = roundEnv.getElementsAnnotatedWith(SourceTargetChannelContent::class.java)

        sourceTargetContentsToGenerate
                .forEach {
                    val annotation = it.getAnnotation(SourceTargetChannelContent::class.java)!!
                    val numeric = annotation.numeric
                    val className = it.simpleName.toString()

                    val parent = ClassName.bestGuess("RplSourceTargetChannelContent")

                    val parameters = listOf(
                            ParameterSpec.builder("source", String::class).build(),
                            ParameterSpec.builder("target", String::class).build(),
                            ParameterSpec.builder("channel", String::class).build(),
                            ParameterSpec.builder("content", String::class).build()
                    )

                    generate(kaleNumericsTypeSpec, numeric, className, parent, parameters)
                }
    }

    fun generate(kaleNumericsTypeSpec: TypeSpec.Builder, numeric: String, className: String, parent: ClassName, parameters: Iterable<ParameterSpec>) {
        val rplTypeSpec = rpl_container(numeric, className, parent, parameters)
        kaleNumericsTypeSpec.addType(rplTypeSpec)
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}