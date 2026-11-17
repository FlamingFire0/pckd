package me.flamingo.pckd.frontend.screens.editor.groupeditor

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.components.tree.TreeGroup
import me.flamingo.pckd.frontend.components.tree.toTree
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.screens.editor.Editor
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme

class GroupEditor(val editor: Editor) : Screen {

    var searchQuery by mutableStateOf("")
    
    fun getAll() {
        editor.rootDirectory
    }
    
    fun reload(): GroupEditor {
        Screen.back()
        return editor.reload().openGroupEditor()
    }
    
    @Composable
    fun TopBar() = Row(Modifier.fillMaxWidth().padding(vertical = 5.dp).height(40.dp), verticalAlignment = Alignment.CenterVertically) {
        
        Spacer(Modifier.width(5.dp))
        editor.TopBarButton("Back", Icons.Rounded.KeyboardDoubleArrowLeft, Screen::back)
        Spacer(Modifier.width(7.dp))
        editor.TopBarButton("Reload", Icons.Rounded.Refresh, ::reload)
        Spacer(Modifier.width(7.dp))
        SearchBar()
        Spacer(Modifier.width(5.dp))
    }

    @Composable
    fun RowScope.SearchBar() {
        val interactionSource = remember { MutableInteractionSource() }
        val textStyle = LocalTextStyle.current.copy(color = Mocha.text)

        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.weight(1f).height(40.dp),
            singleLine = true,
            textStyle = textStyle,
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                TextFieldDefaults.OutlinedTextFieldDecorationBox(
                    value = searchQuery,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    shape = Theme.Shapes.medium,
                    placeholder = { Text("Search Groups") },
                    leadingIcon = {
                        Icon(Icons.Rounded.Search, "Search", Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        IconButton({ searchQuery = "" }, Modifier.size(32.dp)) {
                            Icon(Icons.Rounded.Close, "Clear", Modifier.size(18.dp))
                        }
                    },
                    // zero vertical padding so the 40.dp height fits the text
                    contentPadding = TextFieldDefaults.outlinedTextFieldPadding(
                        top = 0.dp, bottom = 0.dp
                    ),
                )
            },
        )
    }
    
    @Composable
    fun GroupsPane() = editor.EditorPane {
        Column {
            val keys = editor.rootDirectory.allKey.filter { it.toString().contains(searchQuery) }

            TreeGroup(
                editor.rootDirectory.name,
                mergeable = false,
                children = keys.toTree({ it in editor.keys }) {
                    isSelected = !isSelected

                    if(isSelected) editor.addKey(key)
                    else editor.removeKey(key)
                },
            ).content()
        }
    }
    
    @Composable
    override fun Content() = Surface(color = Mocha.crust) {
        Column(Modifier.fillMaxSize().padding(5.5.dp)) {
            TopBar()

            GroupsPane()
        }
    }
}