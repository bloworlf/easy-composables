# easy-composables

Easy-To-Use Composables

Different files containing pieces of code that I use in my Compose projects. They are pretty
customizable (and I'll continue to update them). Feel free to use them and make sure to update them
to your liking/needs.
Some components use other from different file.

_Description and how to use will come later_

# Dialog Component

Add a dialog to your project that can serve multiple purposes.

![DialogComponent](https://github.com/bloworlf/easy-composables/blob/main/images/DialogComponent.png)

| Parameter       |                 Type                 | Nullable | Description |
|-----------------|:------------------------------------:|---------:|------------:|
| modifier        |               Modifier               |        ✓ |           - |
| title           |                String                |        ✓ |             |
| message         |                String                |        ✓ |             |
| icon            |             ImageVector              |        ✓ |             |
| content         | @Composable (ColumnScope.() -> Unit) |        ✓ |             |
| cancellable     |               Boolean                |        ✓ |             |
| positiveText    |                String                |        ✓ |             |
| onPositiveClick |              () -> Unit              |        ✓ |             |
| negativeText    |                String                |        ✓ |             |
| onNegativeClick |              () -> Unit              |        ✓ |             |
| onDismiss       |              () -> Unit              |        ✗ |             |
