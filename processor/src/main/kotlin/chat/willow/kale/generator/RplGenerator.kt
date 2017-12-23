package chat.willow.kale.generator

import chat.willow.kale.generator.message.ICommand
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

annotation class SourceTargetContent(val numeric: String)
annotation class SourceTargetChannelContent(val numeric: String)

object KaleNumericsExample {
    object RPL_ENDOFNAMES: ICommand {

        override val command = "366"

        class Message(source: String, target: String, content: String): RplSourceTargetContent.Message(source, target, content)
        object Parser : RplSourceTargetContent.Parser(command)
        object Serialiser : RplSourceTargetContent.Serialiser(command)
        object Descriptor : RplSourceTargetContent.Descriptor(command, Parser)
    }

    object RPL_CHANNELISFULL : ICommand {

        override val command = "471"

        class Message(source: String, target: String, channel: String, content: String): RplSourceTargetChannelContent.Message(source, target, channel, content)
        object Parser : RplSourceTargetChannelContent.Parser(command)
        object Serialiser : RplSourceTargetChannelContent.Serialiser(command)
        object Descriptor : RplSourceTargetChannelContent.Descriptor(command, Parser)

    }
}

class RplGenerator : AbstractProcessor() {

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf("*")
    }

    fun rpl_container(numeric: String, name: String, parent: KClass<*>): TypeSpec {
        val parentClassName = parent.asClassName()
        val message = ClassName.bestGuess("$parentClassName.Message")
        val parser = ClassName.bestGuess("$parentClassName.Parser")
        val serialiser = ClassName.bestGuess("$parentClassName.Serialiser")
        val descriptor = ClassName.bestGuess("$parentClassName.Descriptor")

        val messageTypeSpec = TypeSpec
                .classBuilder("Message")
                .superclass(message)
                .primaryConstructor(
                        FunSpec
                                .constructorBuilder()
                                .addParameter("source", String::class)
                                .addParameter("target", String::class)
                                .addParameter("content", String::class)
                                .build()
                )
                .addSuperclassConstructorParameter(CodeBlock.of("source, target, content"))
                .build()

        val parserTypeSpec = TypeSpec
                .objectBuilder("Parser")
                .superclass(parser)
                .addSuperclassConstructorParameter(CodeBlock.of("command"))
                .build()

        val serialiserTypeSpec = TypeSpec
                .objectBuilder(serialiser)
                .superclass(RplSourceTargetContent.Serialiser::class)
                .addSuperclassConstructorParameter(CodeBlock.of("command"))
                .build()

        val descriptorTypeSpec = TypeSpec
                .objectBuilder(descriptor)
                .superclass(RplSourceTargetContent.Descriptor::class)
                .addSuperclassConstructorParameter(CodeBlock.of("command, Parser"))
                .build()

        return TypeSpec
                .objectBuilder("RPL_${name.toUpperCase()}")
                .addSuperinterface(ICommand::class)
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

        val fileSpec = FileSpec.get("chat.willow.kale.generator.rpl", kaleNumerics)

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        fileSpec.writeTo(File(kaptKotlinGeneratedDir, ""))

        return true
    }

    fun generateSourceTargetContent(kaleNumericsTypeSpec: TypeSpec.Builder, roundEnv: RoundEnvironment) {
        val sourceTargetContentsToGenerate = roundEnv.getElementsAnnotatedWith(SourceTargetContent::class.java)

        sourceTargetContentsToGenerate
                .forEach {
                    val annotation = it.getAnnotation(SourceTargetContent::class.java)!!
                    val numeric = annotation.numeric
                    val className = it.simpleName.toString()

                    generate(kaleNumericsTypeSpec, numeric, className, RplSourceTargetContent::class)
                }
    }

    fun generateSourceTargetChannelContent(kaleNumericsTypeSpec: TypeSpec.Builder, roundEnv: RoundEnvironment) {
        val sourceTargetContentsToGenerate = roundEnv.getElementsAnnotatedWith(SourceTargetChannelContent::class.java)

        sourceTargetContentsToGenerate
                .forEach {
                    val annotation = it.getAnnotation(SourceTargetChannelContent::class.java)!!
                    val numeric = annotation.numeric
                    val className = it.simpleName.toString()

                    generate(kaleNumericsTypeSpec, numeric, className, RplSourceTargetChannelContent::class)
                }
    }

    fun generate(kaleNumericsTypeSpec: TypeSpec.Builder, numeric: String, className: String, parent: KClass<*>) {
        val rplTypeSpec = rpl_container(numeric, className, RplSourceTargetContent::class)
        kaleNumericsTypeSpec.addType(rplTypeSpec)
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}