package com.example.playground.ui.features

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playground.R

@SuppressLint("DefaultLocale")
@Composable
fun UI(
    modifier: Modifier = Modifier
) {
    // Get the current app context to show things like a "Toast" message.
    val context = LocalContext.current

    // A Column arranges its children vertically from top to bottom.
    Column(
        // Center all the items horizontally within the column.
        horizontalAlignment = Alignment.CenterHorizontally,
        // Arrange items at the top of the column.
        verticalArrangement = Arrangement.Top,
        // These are modifiers that change how the Column looks and behaves.
        modifier = modifier
            // Make the column take up the whole screen.
            .fillMaxSize()
            // Set the background color from the app's theme.
            .background(
                color = MaterialTheme.colorScheme.background
            )
            // Add padding all around the column.
            .padding(16.dp)
            // Make the content scrollable vertically if it gets too long.
            .verticalScroll(rememberScrollState()),
        content = {

            // 'rememberSaveable' saves the state even if the app rotates.
            // 'nameInput' holds the text the user types into the text field.
            var nameInput by rememberSaveable { mutableStateOf("") }

            // These help us control the keyboard and focus on the screen.
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            // A Row arranges its children horizontally.
            Row(
                // Align items in the center vertically.
                verticalAlignment = Alignment.CenterVertically,
                // Add space between each item in the row.
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // This is a text input field with an outline.
                OutlinedTextField(
                    // The text shown in the field comes from 'nameInput'.
                    value = nameInput,
                    // When the text changes, update the 'nameInput' variable.
                    onValueChange = { nameInput = it },
                    // This is the floating label for the text field.
                    label = { Text("New name") },
                    // Makes the text field take up all available space in the row.
                    modifier = Modifier.weight(1f),
                    // Only allow one line of text.
                    singleLine = true,
                    maxLines = 1,
                    // Use a small, rounded shape for the text field.
                    shape = MaterialTheme.shapes.small,
                    // Define the colors for the border of the text field.
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    ),
                    // The hint text that shows when the field is empty.
                    placeholder = { Text("Enter a name") },
                    // An icon on the right side of the text field.
                    trailingIcon = {
                        // Only show the icon if the user has typed something.
                        if (nameInput.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    /* maybe hide password */
                                }
                            ) {
                                // The lock icon.
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Add"

                                )
                            }
                        }
                    },
                    // An icon on the left side of the text field.
                    leadingIcon = {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            // The person icon.
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Add"

                            )
                        }
                    },
                    // Shows an error state if the text is too long.
                    isError = nameInput.isNotEmpty() && nameInput.length > 10,
                    // This text appears below the field when there's an error.
                    supportingText = {
                        if (nameInput.isNotEmpty() && nameInput.length > 10) {
                            Text("Name can't be longer than 10 characters")
                        }
                    },
                    // Define what happens when the "Done" button on the keyboard is pressed.
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Hide the keyboard and clear focus from the text field.
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                    // Configure the keyboard type and behavior.
                    keyboardOptions = KeyboardOptions(
                        // Change the keyboard's "Enter" button to a "Done" button.
                        imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                        // Automatically capitalize the first letter of each word.
                        capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Words
                    )
                )

                // A standard button.
                Button(
                    onClick = {                    }
                ) {
                    Text(
                        "Send"
                    )
                }
            }

            // A Spacer is an empty box used to add space between components.
            Spacer(modifier = Modifier.padding(8.dp))

            // A row for more buttons.
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Different button types with different styles.
                Button(onClick = { /* primary action */ }) { Text("Button") }
                OutlinedButton(onClick = { /* secondary action */ }) { Text("Outlined") }
                TextButton(onClick = { /* tertiary action */ }) { Text("TextButton") }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // A full-width button.
            Button(
                onClick = {
                    // This shows a small popup message (a "Toast") when the button is clicked.
                    Toast.makeText(context, "Login was Successful", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                // Makes the corners of the button rounded.
                shape = RoundedCornerShape(24.dp),
                // Sets the colors for the button itself and its text.
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text(
                    "Login",
                    modifier = Modifier.fillMaxWidth(),
                    // Use a specific text style from the theme.
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    // Make the text bold.
                    fontWeight = FontWeight.Bold,
                    // Center the text inside the button.
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // A row to create a line, text, and another line.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                // A horizontal line. The `weight(1f)` makes it fill available space.
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    thickness = 3.dp
                )

                Spacer(modifier = Modifier.padding(8.dp))

                // The text in the middle.
                Text("OR")

                Spacer(modifier = Modifier.padding(8.dp))

                // The second horizontal line.
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // An outlined button for a "Google" sign-in.
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Google button click", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
            ) {
                // A row to hold the icon and the text side-by-side.
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // An icon loaded from the app's drawable resources.
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google",
                        modifier = Modifier
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Google",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            // A button that looks like plain text.
            TextButton(
                onClick = { /* navigate to forgot password screen */ }
            ) {
                Text(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }

            // 'switchOn' and 'sliderValue' are state variables that remember their values.
            var switchOn by rememberSaveable { mutableStateOf(false) }
            var sliderValue by rememberSaveable { mutableStateOf(0.5f) }

            Spacer(modifier = Modifier.padding(8.dp))

            // A row to hold the switch, slider, and their labels.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Switch:")
                // A switch that changes the `switchOn` state when clicked.
                Switch(checked = switchOn, onCheckedChange = { switchOn = it })

                Spacer(modifier = Modifier.width(16.dp))

                Text("Slider:")
                // A slider that changes the `sliderValue` state.
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    modifier = Modifier.width(150.dp)
                )

                // Text that shows the current value of the slider, formatted to two decimal places.
                Text(
                    text = String.format("%.2f", sliderValue),
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text ="Names list (checkboxes + radio selection)",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // State variables for the checkbox and radio button.
            var isCheckboxSelected by rememberSaveable { mutableStateOf(false) }
            var isRadioSelected by rememberSaveable { mutableStateOf(false) }

            // A radio button. It's 'selected' when `isRadioSelected` is true.
            RadioButton(
                selected = isRadioSelected,
                onClick = {
                    // When clicked, set its state to true.
                    isRadioSelected = true
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // A checkbox. Its 'checked' state is controlled by `isCheckboxSelected`.
            Checkbox(
                checked = isCheckboxSelected,
                // When its state changes, update the `isCheckboxSelected` variable.
                onCheckedChange = {
                    isCheckboxSelected = it
                }
            )

            // A spacer that pushes the next item to the very bottom of the column.
            Spacer(modifier = Modifier.weight(1f))

            // Footer text at the bottom.
            Text(
                text = "Built with ❤️ in Compose by @AndroidDev",
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color(0xFF999999),
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }
    )
}