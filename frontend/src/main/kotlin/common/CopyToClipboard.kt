package me.flamingo.pckd.frontend.common

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection


fun copyToClipboard(text: String) {
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
}