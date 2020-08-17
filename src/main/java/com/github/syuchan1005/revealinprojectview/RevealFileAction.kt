package com.github.syuchan1005.revealinprojectview

import com.intellij.ide.FileEditorProvider
import com.intellij.ide.SelectInContext
import com.intellij.ide.actions.ShowFilePathAction
import com.intellij.ide.projectView.ProjectView
import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

class RevealFileAction : DumbAwareAction() {
    override fun update(e: AnActionEvent) {
        val project = e.project ?: return
        val projectView = ProjectView.getInstance(project)
        val file = ShowFilePathAction.findLocalFile(CommonDataKeys.VIRTUAL_FILE.getData(e.dataContext))
        val presentation = e.presentation
        presentation.isEnabled = projectView.currentViewId != ProjectViewPane.ID && file != null && !file.isDirectory
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (project == null || virtualFile == null) return
        val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: return
        val projectView = ProjectView.getInstance(project)
        projectView.changeView(ProjectViewPane.ID)
        val currentProjectViewPane = projectView.currentProjectViewPane
        val selectInTarget = currentProjectViewPane.createSelectInTarget()
        selectInTarget.selectIn(MySelectInContext(psiFile, project), false)
    }

    class MySelectInContext(private val psiFile: PsiFile, private val myProject: Project) : SelectInContext {
        override fun getProject(): Project {
            return myProject
        }

        override fun getFileEditorProvider(): FileEditorProvider {
            return FileEditorProvider { FileEditorManager.getInstance(project).openFile(virtualFile, false)[0] }
        }

        private val psiElement: PsiElement
            get() = psiFile

        override fun getVirtualFile(): VirtualFile {
            return psiFile.virtualFile
        }

        override fun getSelectorInFile(): Any? {
            return psiElement
        }

    }
}
