package io.sajensen.specmate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class GoToTestAction : AnAction("Spec") {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val currentFile = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)

        if (project != null && currentFile != null) {
            val testFilePath = getSpecPath(currentFile)
            val testFile = LocalFileSystem.getInstance().findFileByPath(testFilePath)

            if (testFile != null) {
                FileEditorManager.getInstance(project).openFile(testFile, true)
            } else {
                Messages.showMessageDialog(
                        project,
                        "Test file not found: $testFilePath",
                        "File Not Found",
                        Messages.getInformationIcon()
                )
            }
        }
    }

    private fun getSpecPath(file: VirtualFile): String {
        val basePath = file.path.replaceFirst("src", "spec").substringBeforeLast(".")
        val extension = file.extension
        return basePath.plus("_spec.").plus(extension)
    }

    override fun update(e: AnActionEvent) {
        super.update(e)

        val project = e.project
        val file = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)
        val isClojureFile = file?.extension in listOf("clj", "cljc", "cljs")

        e.presentation.isEnabledAndVisible = project != null && isClojureFile
    }
}