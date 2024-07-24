
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.table.*;
import java.sql.*;
import java.util.Map;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.renderer.category.BarRenderer;
public class LoginFrame extends JFrame {
 private JTextField usernameField;
 private JPasswordField passwordField;
 //Login panel
 public LoginFrame() {
 setTitle("Login");
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 setSize(800, 500);
 setLocationRelativeTo(null);
 setLayout(new BorderLayout());
 // Create the login form components
 JLabel usernameLabel = new JLabel("Username:");
 JLabel passwordLabel = new JLabel("Password:");
 usernameField = new JTextField(10);
 passwordField = new JPasswordField(10);
 JButton loginButton = new JButton("Login");
 JPanel formPanel = new JPanel(new GridLayout(5, 1));
 formPanel.add(usernameLabel);
 formPanel.add(usernameField);
 formPanel.add(passwordLabel);
 formPanel.add(passwordField);
 formPanel.add(new JLabel()); // Placeholder for spacing
 formPanel.add(loginButton);
 add(formPanel, BorderLayout.CENTER);
 // Login button action listener
 loginButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 String username = usernameField.getText();
 String password = new String(passwordField.getPassword());
 if (login(username, password)) {
 // Successful login
 JOptionPane.showMessageDialog(LoginFrame.this, "Login successful!");
 // Open the admin dashboard
 openAdminDashboard();
 } else {
 // Invalid login
 JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password!");
 }
 }
 });
 }
 //Method for validating username and password from the login table
 private boolean login(String username, String password) {
 // JDBC connection details
 String url = "jdbc:mysql://localhost:3306/pharmacy";
 String user = "root";
 String pass = "jawahar123";
 try {
 // Establish database connection
 Connection connection = DriverManager.getConnection(url, user, pass);
 
 // Prepare the SQL statement
 String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
 PreparedStatement statement = connection.prepareStatement(sql);
 statement.setString(1, username);
 statement.setString(2, password);
 
 // Execute the query
 ResultSet resultSet = statement.executeQuery();
 // Check if the query returned any rows
 while (resultSet.next()) {
 // Valid login credentials
 String dbUsername = resultSet.getString("username");
 String dbPassword = resultSet.getString("password");
 // Compare the retrieved username and password with the provided ones
 if (username.equals(dbUsername) && password.equals(dbPassword)) {
 connection.close();
 return true;
 }
 }
 connection.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 return false;
 }
 
 //Admin Dashboard Panel
 public void openAdminDashboard() {
 getContentPane().removeAll();
 setTitle("Admin Dashboard");
 
 //Create the admin dashboard panel
 JPanel adminDashboardPanel = new JPanel(new BorderLayout());
 JPanel column1Panel = new JPanel(new GridLayout(0,1));
 column1Panel.setPreferredSize(new Dimension(getWidth()*40/100,getHeight()));
 
 //Add the heading
 JLabel adminMenuLabel = new JLabel("Admin Menu");
 column1Panel.add(adminMenuLabel);
 
 //Create and add buttons to col 1
 String[] buttonLabels = {"Add Medicine", "Medicine Report", "Add Sales", "Stock Report", "Logout and 
Exit"};
 for(String label : buttonLabels) {
 JButton button = new JButton(label);
 button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
 button.setFocusPainted(false); //Remove focus border
 button.addMouseListener(new java.awt.event.MouseAdapter() {
 public void mouseEntered(java.awt.event.MouseEvent evt) {
 button.setBorder(BorderFactory.createLineBorder(Color.BLUE));
 }
 public void mouseExited(java.awt.event.MouseEvent evt) {
 button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
 }
 });
 column1Panel.add(button);
 
 // Action listener for each button
 button.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 // Get the button label
 String buttonLabel = button.getText();
 // Open the corresponding panel based on the button clicked
 if (buttonLabel.equals("Add Medicine")) {
 openMedicineManagement();
 } else if (buttonLabel.equals("Medicine Report")) {
 openShowTable();
 }else if (buttonLabel.equals("Add Sales")) {
 openSalesDashboard();
 }else if (buttonLabel.equals("Stock Report")) {
 displayStocksBarChart();
 }else if (buttonLabel.equals("Logout and Exit")) {
 System.exit(0);
 }
 }
 });
 }
 
 adminDashboardPanel.add(column1Panel,BorderLayout.WEST);
 
 //Create the column 2 panel
 JPanel column2Panel = new JPanel(new BorderLayout());
 column2Panel.setPreferredSize(new Dimension(getWidth()*60/100,getHeight()));
 JLabel dashboardLabel = new JLabel("Dashboard");
 dashboardLabel.setHorizontalAlignment(JLabel.CENTER);
 column2Panel.add(dashboardLabel, BorderLayout.NORTH);
 
 // Add the image to column 2 
 ImageIcon image = new ImageIcon("D://HTML//CSP_Project_V1//public//images//2927262.jpg");
 JLabel imageLabel = new JLabel(image);
 column2Panel.add(imageLabel, BorderLayout.CENTER);
 adminDashboardPanel.add(column2Panel, BorderLayout.CENTER);
 add(adminDashboardPanel);
 revalidate();
 repaint();
 }
 //Add medicine page
 private void openMedicineManagement() {
 final boolean isEditArr[] = {false};
 final String[] idCheckArr = {"0"};
 getContentPane().removeAll();
 setTitle("Medicine Management");
 // Create the Medicine Management panel
 JPanel medicineManagementPanel = new JPanel(new BorderLayout());
 // Create the form panel (row 1)
 JPanel formPanel = new JPanel(new GridLayout(6, 2));
 JLabel nameLabel = new JLabel("Medicine Name:");
 JTextField nameField = new JTextField();
 JLabel compNameLabel = new JLabel("Company Name:");
 JTextField compNameField = new JTextField();
 JLabel priceLabel = new JLabel("Cost per Unit:");
 JTextField priceField = new JTextField();
 JLabel quantityLabel = new JLabel("Add Stock:");
 JTextField quantityField = new JTextField();
 JLabel typeLabel = new JLabel("Type:");
 JTextField typeField = new JTextField();
 JButton saveButton = new JButton("Save Medicine");
 JButton cancelButton = new JButton("Cancel");
 formPanel.add(nameLabel);
 formPanel.add(nameField);
 formPanel.add(compNameLabel);
 formPanel.add(compNameField);
 formPanel.add(priceLabel);
 formPanel.add(priceField);
 formPanel.add(quantityLabel);
 formPanel.add(quantityField);
 formPanel.add(typeLabel);
 formPanel.add(typeField);
 formPanel.add(cancelButton);
 formPanel.add(saveButton);
 medicineManagementPanel.add(formPanel, BorderLayout.NORTH);
 // Create the table panel (row 2)
 JPanel tablePanel = new JPanel(new BorderLayout());
 JTable medicineTable = new JTable(); // Create the medicine table
 medicineTable.setModel(new DefaultTableModel());
 
 refreshMedicineTable(medicineTable); // Fetch and populate the table data
 JScrollPane scrollPane = new JScrollPane(medicineTable);
 tablePanel.add(scrollPane, BorderLayout.CENTER);
 medicineManagementPanel.add(tablePanel, BorderLayout.CENTER);
 add(medicineManagementPanel); // Add the Medicine Management panel to the frame
 
 JButton goToDashboardButton = new JButton("Go to Dashboard"); // Add the "Go to Dashboard" button at the 
bottom
 goToDashboardButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 openAdminDashboard();
 }
 });
 
 medicineManagementPanel.add(goToDashboardButton, BorderLayout.SOUTH);
 revalidate();
 repaint();
 // Save button action listener
 saveButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 String medicineName = nameField.getText();
 String compName = compNameField.getText();
 String price = priceField.getText();
 String quantity = quantityField.getText();
 String medicineType = typeField.getText();
 // Validate and save the medicine to the database
 if (validateMedicineFields(medicineName, compName, price, quantity, medicineType)) {
 saveMedicine(idCheckArr[0],medicineName, compName, price, quantity, medicineType, isEditArr[0]);
 refreshMedicineTable(medicineTable);
 clearFormFields(nameField, compNameField, priceField, quantityField, typeField);
 }
 else {
 // Clear the form fields if validation fails
 clearFormFields(nameField, compNameField, priceField, quantityField, typeField);
 }
 }
 });
 // Cancel button action listener
 cancelButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 clearFormFields(nameField, compNameField, priceField, quantityField, typeField);
 }
 });
 
 
 
 medicineTable.getSelectionModel().addListSelectionListener(e -> {
 int selectedRow = medicineTable.getSelectedRow();
 if (selectedRow != -1) {
 int medicineId = (int) medicineTable.getValueAt(selectedRow, 0); // ID is in the first column
 // Show the dialog box with Edit and Delete buttons
 String[] options = { "Edit", "Delete" };
 int choice = JOptionPane.showOptionDialog(medicineManagementPanel, "Choose an action:", "Action",
 JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
 if (choice == 0) {
 // Edit button selected
 Map<String, Object> medicineDetails = editDeleteMedicine(medicineId, true);
 if (medicineDetails != null) {
 idCheckArr[0] = Integer.toString(medicineId);
 nameField.setText((String) medicineDetails.get("name"));
 compNameField.setText((String) medicineDetails.get("compName"));
 priceField.setText(medicineDetails.get("price").toString());
 quantityField.setText(medicineDetails.get("stock").toString());
 typeField.setText((String) medicineDetails.get("type"));
 isEditArr[0] = true;
 }
 } else if (choice == 1) {
 // Delete button selected
 int confirmDialogResult = JOptionPane.showConfirmDialog(medicineManagementPanel,
 "Are you sure you want to delete this medicine?", "Confirmation", 
JOptionPane.YES_NO_OPTION);
 if (confirmDialogResult == JOptionPane.YES_OPTION) {
 editDeleteMedicine(medicineId, false);
 refreshMedicineTable(medicineTable);
 }
 }
 }
 });
 }
 //Refreshing and loading the medicine each time if modified
 private void refreshMedicineTable(JTable medicineTable) {
 DefaultTableModel model = (DefaultTableModel) medicineTable.getModel();
 model.setColumnIdentifiers(new Object[]{"ID", "Name", "Company", "Price", "Stock", "Type"});
 model.setRowCount(0); // Clear the existing table data
 // JDBC connection details
 String url = "jdbc:mysql://localhost:3306/pharmacy";
 String user = "root";
 String pass = "jawahar123";
 try {
 // Establish database connection
 Connection connection = DriverManager.getConnection(url, user, pass);
 // Execute the query to retrieve medicine data
 String sql = "SELECT * FROM medicines";
 PreparedStatement statement = connection.prepareStatement(sql);
 ResultSet resultSet = statement.executeQuery();
 // Iterate over the result set and add the data to the table model
 while (resultSet.next()) {
 int id = resultSet.getInt("id");
 String name = resultSet.getString("name");
 String compName = resultSet.getString("compname");
 double price = resultSet.getDouble("price");
 int stock = resultSet.getInt("stock");
 String type = resultSet.getString("type");
 // Add the data to the table model
 model.addRow(new Object[]{id, name, compName, price, stock, type});
 }
 connection.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 //Validating all the textfield data
 private boolean validateMedicineFields(String name, String compName, String price, String quantity, String type) {
 // Check if any field is empty
 if (name.isEmpty() || compName.isEmpty() || price.isEmpty() || quantity.isEmpty() || type.isEmpty()) {
 JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", 
JOptionPane.ERROR_MESSAGE);
 return false;
 }
 // Check if price and quantity are numeric
 try {
 double parsedPrice = Double.parseDouble(price);
 int parsedQuantity = Integer.parseInt(quantity);
 
 // Check if price and quantity are positive
 if (parsedPrice <= 0 || parsedQuantity <= 0) {
 JOptionPane.showMessageDialog(this, "Please enter a positive value for price and quantity.", 
"Validation Error", JOptionPane.ERROR_MESSAGE);
 return false;
 }
 } catch (NumberFormatException e) {
 JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price and quantity.", 
"Validation Error", JOptionPane.ERROR_MESSAGE);
 return false;
 }
 return true;
}
 
 //If data are validated, save the new medicine data to the database
 private void saveMedicine(String id,String name, String compName, String price, String quantity, String type, 
boolean isEditArr) {
 // JDBC connection details
 String url = "jdbc:mysql://localhost:3306/pharmacy";
 String user = "root";
 String pass = "jawahar123";
 try {
 // Establish database connection
 Connection connection = DriverManager.getConnection(url, user, pass);
 if(isEditArr) { //If edit functionality, we have to add the given stocks to the old stocks.
 int newStock = 0;
 String selsql = "SELECT stock FROM medicines where id=?";
 PreparedStatement selectStatement1 = connection.prepareStatement(selsql);
 selectStatement1.setInt(1,Integer.parseInt(id));
 ResultSet resultSet1 = selectStatement1.executeQuery();
 if(resultSet1.next()) {
 int existingStock = resultSet1.getInt("stock");
 newStock = existingStock + Integer.parseInt(quantity);
 }
 String updateSql = "UPDATE medicines SET name = ?,compname=?,price=?,stock=?,type=? WHERE id= 
?";
 PreparedStatement updateStatement = connection.prepareStatement(updateSql);
 updateStatement.setString(1, name);
 updateStatement.setString(2, compName);
 updateStatement.setString(3, price);
 updateStatement.setInt(4, newStock);
 updateStatement.setString(5, type);
 updateStatement.setInt(6, Integer.parseInt(id));
 updateStatement.executeUpdate();
 JOptionPane.showMessageDialog(this, "Medicine updated successfully!");
 
 }
 else {
 // Check if the medicine already exists in the database
 String selectSql = "SELECT * FROM medicines WHERE name = ? AND compname = ?";
 PreparedStatement selectStatement = connection.prepareStatement(selectSql);
 selectStatement.setString(1, name);
 selectStatement.setString(2, compName);
 ResultSet resultSet = selectStatement.executeQuery();
 
 if (resultSet.next()) {
 // Medicine already exists and also it is not edited.
 JOptionPane.showMessageDialog(this, "Medicine already exists! Please select Cancel and try 
again!");
 
 } else {
 // Medicine does not exist, insert a new row
 String insertSql = "INSERT INTO medicines (name, compname, price, stock, type) VALUES (?, ?, ?, 
?, ?)";
 PreparedStatement insertStatement = connection.prepareStatement(insertSql);
 insertStatement.setString(1, name);
 insertStatement.setString(2, compName);
 insertStatement.setString(3, price);
 insertStatement.setInt(4, Integer.parseInt(quantity));
 insertStatement.setString(5, type);
 insertStatement.executeUpdate();
 JOptionPane.showMessageDialog(this, "Medicine added successfully!");
 }
 }
 connection.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 
 //If flag true do edit, else do delete operation
 private Map<String, Object> editDeleteMedicine(int medicineId, boolean flag) {
 // JDBC connection details
 String url = "jdbc:mysql://localhost:3306/pharmacy";
 String user = "root";
 String pass = "jawahar123";
 try {
 
 Connection connection = DriverManager.getConnection(url, user, pass);
 if (!flag) {
 // Prepare the SQL statement to delete the medicine
 String deleteSql = "DELETE FROM medicines WHERE id = ?";
 PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
 deleteStatement.setInt(1, medicineId);
 deleteStatement.executeUpdate();
 JOptionPane.showMessageDialog(this, "Medicine deleted successfully!");
 return null; // Return null when deleting a medicine
 } else {
 String query = "SELECT * FROM medicines WHERE id = ?";
 PreparedStatement statement = connection.prepareStatement(query);
 statement.setInt(1, medicineId);
 ResultSet resultSet = statement.executeQuery();
 if (resultSet.next()) {
 // Retrieve the medicine details from the result set
 int id = resultSet.getInt("id");
 String name = resultSet.getString("name");
 String compName = resultSet.getString("compname");
 int price = resultSet.getInt("price");
 int stock = resultSet.getInt("stock");
 String type = resultSet.getString("type");
 // Create a new Map to store the medicine details
 Map<String, Object> medicineDetails = new HashMap<>();
 medicineDetails.put("id", id);
 medicineDetails.put("name", name);
 medicineDetails.put("compName", compName);
 medicineDetails.put("price", price);
 medicineDetails.put("stock", stock);
 medicineDetails.put("type", type);
 // Return the medicine details as a Map
 return medicineDetails;
 }
 }
 connection.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 return null; // Return null if no medicine is found or an error occurs
 }
 //Method for clearing the fields
 private void clearFormFields(JTextField... fields) {
 for (JTextField field : fields) {
 field.setText("");
 }
 
 
 //Open Medicine Table page
 private void openShowTable() {
 // Remove all existing components from the panel
 getContentPane().removeAll();
 setTitle("All Medicine Reports");
 // Create the panel to hold the medicine table
 JPanel showTablePanel = new JPanel(new BorderLayout());
 showTablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 // Create the table model
 DefaultTableModel tableModel = new DefaultTableModel();
 tableModel.addColumn("ID");
 tableModel.addColumn("Name");
 tableModel.addColumn("Company Name");
 tableModel.addColumn("Price");
 tableModel.addColumn("Stock");
 tableModel.addColumn("Type");
 // Retrieve data from the database and add it to the table model
 String url = "jdbc:mysql://localhost:3306/pharmacy";
 String user = "root";
 String password = "jawahar123";
 try {
 Connection connection = DriverManager.getConnection(url, user, password);
 Statement statement = connection.createStatement();
 String sql = "SELECT * FROM medicines";
 ResultSet resultSet = statement.executeQuery(sql);
 while (resultSet.next()) {
 int id = resultSet.getInt("id");
 String name = resultSet.getString("name");
 String companyName = resultSet.getString("compname");
 int price = resultSet.getInt("price");
 int stock = resultSet.getInt("stock");
 String type = resultSet.getString("type");
 Object[] rowData = {id, name, companyName, price, stock, type};
 tableModel.addRow(rowData);
 }
 connection.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 // Create the table with the table model
 JTable medicineTable = new JTable(tableModel);
 medicineTable.setGridColor(Color.GRAY);
 medicineTable.setShowGrid(true);
 // Create a custom cell renderer for the column headings
 DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
 headerRenderer.setBackground(Color.YELLOW);
 headerRenderer.setForeground(Color.BLUE);
 headerRenderer.setFont(headerRenderer.getFont().deriveFont(Font.BOLD));
 // Apply the cell renderer to the column headings
 for (int i = 0; i < medicineTable.getColumnModel().getColumnCount(); i++) {
 medicineTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
 }
 // Create a scroll pane and add the table to it
 JScrollPane scrollPane = new JScrollPane(medicineTable);
 // Add the scroll pane to the panel
 showTablePanel.add(scrollPane, BorderLayout.CENTER);
 
 // Add the "Go to Dashboard" button at the bottom
 JButton goToDashboardButton = new JButton("Go to Dashboard");
 goToDashboardButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 openAdminDashboard();
 }
 });
 showTablePanel.add(goToDashboardButton, BorderLayout.SOUTH);
 add(showTablePanel);
 // Repaint the frame to update the changes
 revalidate();
 repaint();
 }
 
 //Customer billing page
 public void openSalesDashboard() {
 getContentPane().removeAll();
 setTitle("Sales Dashboard");
 
 // Create the sales dashboard panel
 JPanel salesDashboardPanel = new JPanel(new GridLayout(3, 1));
 // Row 1: Customer Details
 JPanel row1Panel = new JPanel(new GridLayout(3, 2));
 JLabel customerIdLabel = new JLabel("Customer ID:");
 JTextField customerIdTextField = new JTextField();
 JLabel customerNameLabel = new JLabel("Customer Name:");
 JTextField customerNameTextField = new JTextField();
 JLabel customerPhoneLabel = new JLabel("Customer Phone:");
 JTextField customerPhoneTextField = new JTextField();
 row1Panel.add(customerIdLabel);
 row1Panel.add(customerIdTextField);
 row1Panel.add(customerNameLabel);
 row1Panel.add(customerNameTextField);
 row1Panel.add(customerPhoneLabel);
 row1Panel.add(customerPhoneTextField);
 salesDashboardPanel.add(createTitledPanel("Customer Details", row1Panel));
 // Row 2: Sales Dashboard
 JPanel row2Panel = new JPanel(new BorderLayout());
 JPanel inputPanel = new JPanel(new GridLayout(1, 2));
 JLabel selectMedicineLabel = new JLabel("Select Medicine:");
 JComboBox<String> selectMedicineComboBox = new JComboBox<>(getMedicineNames());
 JLabel quantityLabel = new JLabel("Quantity:");
 JTextField quantityTextField = new JTextField();
 JButton addItemButton = new JButton("Add Item");
 inputPanel.add(selectMedicineLabel);
 inputPanel.add(selectMedicineComboBox);
 inputPanel.add(quantityLabel);
 inputPanel.add(quantityTextField);
 inputPanel.add(addItemButton);
 row2Panel.add(inputPanel, BorderLayout.NORTH);
 
 // Create the row 3 panel
 JPanel row3Panel = new JPanel(new BorderLayout());
 
 // Create the table for displaying sales details in row 3
 String[] columnNames = {"ID", "Medicine Name", "Cost per unit", "Quantity", "Total"};
 DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
 JTable salesTable = new JTable(tableModel);
 JScrollPane tableScrollPane = new JScrollPane(salesTable);
 row3Panel.add(tableScrollPane, BorderLayout.CENTER);
 
 // Create the total label
 JLabel totalLabel = new JLabel("Grand Total: 0");
 JButton saveOrderButton = new JButton("Save order and Exit");
 
 // Create the panel for total label and save order button
 JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
 totalPanel.add(totalLabel);
 totalPanel.add(saveOrderButton);
 row3Panel.add(totalPanel, BorderLayout.SOUTH);
 salesDashboardPanel.add(createTitledPanel("Sales Dashboard", row2Panel));
 salesDashboardPanel.add(row3Panel, BorderLayout.SOUTH);
 add(salesDashboardPanel);
 revalidate();
 repaint();
 
 // Action listener for the "Add Item" button
 addItemButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 String selectedMedicine = selectMedicineComboBox.getSelectedItem().toString();
 String quantity = quantityTextField.getText();
 int typedStock = Integer.parseInt(quantity);
 // Fetch the medicine details from the HashMap based on the selected medicine name
 Object[] medicineDetails = getMedicineDetailsByName(selectedMedicine);
 if (medicineDetails != null) {
 int id = (int) medicineDetails[0];
 String name = (String) medicineDetails[1];
 String compname = (String) medicineDetails[2];
 int price = (int) medicineDetails[3];
 int stock = (int) medicineDetails[4];
 String type = (String) medicineDetails[5];
 
 if(stock<typedStock) {
 JOptionPane.showMessageDialog(salesDashboardPanel, "Out of Stock! Available stock: " + stock);
 }
 else {
 // Update the table in Row3 with the fetched medicine details and calculated total
 DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
 Object[] rowData = {id, name, price, quantity, price*Integer.parseInt(quantity)};
 model.addRow(rowData);
 // Calculate and display the total price
 int totalPrice = calculateTotalPrice(model);
 totalLabel.setText("Grand Total: " + Integer.toString(totalPrice));
 
 // Update the stock value in the database
 int updatedStock = stock - typedStock;
 updateStockInDatabase(id, updatedStock,salesDashboardPanel);
 }
 } else {
 // Medicine details not found
 JOptionPane.showMessageDialog(salesDashboardPanel, "Medicine details not found!");
 }
 }
 });
 // Action listener for the "Save order and Exit" button
 saveOrderButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 int customerId = Integer.parseInt(customerIdTextField.getText());
 String customerName = customerNameTextField.getText();
 String customerPhone = customerPhoneTextField.getText();
 
 try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", 
"root", "jawahar123")) {
 String orderSql = "INSERT INTO orders (userid, name, phone, mid, quantity, total) VALUES (?, ?, ?, ?, 
?, ?)";
 PreparedStatement orderStatement = connection.prepareStatement(orderSql);
 // Retrieve the data from the table in the panel
 DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
 int rowCount = model.getRowCount();
 for (int i = 0; i < rowCount; i++) {
 
 int medicineId = Integer.parseInt(model.getValueAt(i, 0).toString());
 int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());
 int total = Integer.parseInt(model.getValueAt(i, 4).toString());
 // Set the parameter values in the prepared statement
 orderStatement.setInt(1, customerId);
 orderStatement.setString(2, customerName);
 orderStatement.setString(3, customerPhone);
 orderStatement.setInt(4, medicineId);
 orderStatement.setInt(5, quantity);
 orderStatement.setInt(6, total);
 // Execute the INSERT statement for order details
 orderStatement.executeUpdate();
 }
 // Close the statements
// orderStatement.close();
 orderStatement.close();
 // Display a success message or any relevant feedback to the user
 JOptionPane.showMessageDialog(salesDashboardPanel, "Order saved successfully!");
 // Clear the table in the panel
 model.setRowCount(0);
 totalLabel.setText("Grand Total: 0");
 openAdminDashboard();
 
 } catch (SQLException e1) {
 // Handle any errors that may occur during the database operation
 e1.printStackTrace();
 JOptionPane.showMessageDialog(salesDashboardPanel, "Error saving order: " + e1.getMessage());
 }
 
 }
 });
 }
 
 //Getting the medicine names for Combo Box
 private String[] getMedicineNames() {
 String[] medicineNames = null;
 try {
 Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", 
"jawahar123");
 String sql = "SELECT name FROM medicines";
 PreparedStatement statement = connection.prepareStatement(sql);
 ResultSet resultSet = statement.executeQuery();
 
 ArrayList<String> nameList = new ArrayList<>();
 while (resultSet.next()) {
 String name = resultSet.getString("name");
 nameList.add(name);
 }
 medicineNames = nameList.toArray(new String[0]);
 connection.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 return medicineNames;
 }
 
 // Declare a HashMap to store the medicine details
 HashMap<String, Object[]> medicineMap = new HashMap<>();
 
 // Method to get the medicine details by name from the HashMap
 private Object[] getMedicineDetailsByName(String name) {
 fetchMedicineDetails();
 return medicineMap.get(name);
 }
 
 // Method to fetch the medicine details by name and store in the HashMap
 private void fetchMedicineDetails() {
 try {
 // Establish database connection
 Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", 
"jawahar123");
 // Prepare the SQL statement to retrieve all medicine details
 String sql = "SELECT * FROM medicines";
 PreparedStatement statement = connection.prepareStatement(sql);
 // Execute the query
 ResultSet resultSet = statement.executeQuery();
 // Iterate through the result set and store the medicine details in the HashMap
 while (resultSet.next()) {
 int id = resultSet.getInt("id");
 String name = resultSet.getString("name");
 String compname = resultSet.getString("compname");
 int price = resultSet.getInt("price");
 int stock = resultSet.getInt("stock");
 String type = resultSet.getString("type");
 // Create an array with the medicine details
 Object[] medicineDetails = {id, name, compname, price, stock, type};
 // Store the medicine details in the HashMap with the name as the key
 medicineMap.put(name, medicineDetails);
 }
 connection.close();
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 //Method to reduce the stocks after ordering
 private void updateStockInDatabase(int medicineId, int updatedStock, JPanel salesDashboardPanel) {
 try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", 
"jawahar123")) {
 String updateSql = "UPDATE medicines SET stock = ? WHERE id = ?";
 PreparedStatement updateStatement = connection.prepareStatement(updateSql);
 // Set the parameter values in the prepared statement
 updateStatement.setInt(1, updatedStock);
 updateStatement.setInt(2, medicineId);
 // Execute the update statement
 updateStatement.executeUpdate();
 // Close the statement
 updateStatement.close();
 } catch (SQLException e) {
 // Handle any errors that may occur during the database operation
 e.printStackTrace();
 JOptionPane.showMessageDialog(salesDashboardPanel, "Error updating stock: " + e.getMessage());
 }
 }
 // Calculate the total price based on the table data
 private int calculateTotalPrice(DefaultTableModel model) {
 int totalPrice = 0;
 for (int row = 0; row < model.getRowCount(); row++) {
 int total = (int) model.getValueAt(row, 4);
 totalPrice += total;
 }
 return totalPrice;
 }
 //To display the Bar chart for Stocks available
 public void displayStocksBarChart() {
 // Get the medicine stocks data
 DefaultCategoryDataset dataset = getMedicineStocksData();
 // Create the bar chart
 JFreeChart chart = ChartFactory.createBarChart(
 "Medicine Stocks", // Chart title
 "Medicine", // X-axis label
 "Stock Quantity", // Y-axis label
 dataset, // Data
 PlotOrientation.VERTICAL, // Orientation
 true, // Include legend
 true, // Include tooltips
 false // Include URLs
 );
 // Customize the chart appearance
 CategoryPlot plot = chart.getCategoryPlot();
 CategoryAxis axis = plot.getDomainAxis();
 axis.setCategoryMargin(0.5);
 BarRenderer renderer = (BarRenderer) plot.getRenderer();
 renderer.setItemMargin(0.05);
 // Create a chart panel and display the chart
 ChartPanel chartPanel = new ChartPanel(chart);
 ChartFrame frame = new ChartFrame("Medicine Stocks", chart);
 frame.setContentPane(chartPanel);
 frame.setSize(800, 600);
 frame.setVisible(true);
 }
 // Method to fetch the medicine stocks data from the "medicines" table
 private DefaultCategoryDataset getMedicineStocksData() {
 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
 // Establish a connection to the database
 try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", 
"jawahar123")) {
 // Prepare the SQL statement to retrieve medicine stocks data
 String sql = "SELECT name, stock FROM medicines";
 PreparedStatement statement = connection.prepareStatement(sql);
 // Execute the SQL statement and retrieve the results
 ResultSet resultSet = statement.executeQuery();
 // Populate the dataset with the retrieved data
 while (resultSet.next()) {
 String medicineName = resultSet.getString("name");
 int stockQuantity = resultSet.getInt("stock");
 dataset.addValue(stockQuantity, "Stocks", medicineName);
 }
 // Close the result set and statement
 resultSet.close();
 statement.close();
 } catch (SQLException e) {
 e.printStackTrace();
 // Handle any errors that may occur during the database operation
 }
 return dataset;
 }
 //Method to create a titled Panel
 private JPanel createTitledPanel(String title, JPanel contentPanel) {
 JPanel titledPanel = new JPanel(new BorderLayout());
 titledPanel.setBorder(BorderFactory.createTitledBorder(title));
 titledPanel.add(contentPanel, BorderLayout.CENTER);
 return titledPanel;
 }
 
 //Main Method
 public static void main(String[] args) {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 LoginFrame loginFrame = new LoginFrame();
 loginFrame.setVisible(true);
 }
 });
 }
}
