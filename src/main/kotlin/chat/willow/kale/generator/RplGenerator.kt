package chat.willow.kale.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@Target(AnnotationTarget.CLASS)
annotation class SourceTargetContent(val numeric: String)

@Target(AnnotationTarget.CLASS)
annotation class SourceTargetChannelContent(val numeric: String)

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("chat.willow.kale.generator.SourceTargetContent", "chat.willow.kale.generator.SourceTargetChannelContent")
@SupportedOptions(RplGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class RplGenerator : AbstractProcessor() {

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        throw RuntimeException("fake failure")
        for (i in 1..1000) {
            throw RuntimeException("fake failure")
        }
        println("process")
        roundEnv.getElementsAnnotatedWith(SourceTargetContent::class.java)
                .forEach {
                    val className = it.simpleName.toString()
                    println("processing $className")
                    val pack = processingEnv.elementUtils.getPackageOf(it).toString()
                    generateClass(className, pack)
                }
        return true
    }

    private fun generateClass(className: String, pack: String) {
        val fileName = "Generated_$className"
        val file = FileSpec.builder(pack, fileName)
                .addType(TypeSpec.classBuilder(fileName)
                        .addFunction(FunSpec.builder("getName")
                                .addStatement("return \"World\"")
                                .build())
                        .build())
                .build()

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.writeTo(File(kaptKotlinGeneratedDir, "$fileName.kt"))
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}