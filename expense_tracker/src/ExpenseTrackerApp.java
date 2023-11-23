import javax.swing.JOptionPane;
import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import view.ExpenseTrackerView;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;

public class ExpenseTrackerApp {

  /**
   * @param args
   */
  public static void main(String[] args) {
    
    // Create MVC components
    ExpenseTrackerModel model = new ExpenseTrackerModel();
    ExpenseTrackerView view = new ExpenseTrackerView();
    model.register(view);
    ExpenseTrackerController controller = new ExpenseTrackerController(model);
    

    // Initialize view
    view.setVisible(true);



    // Handle add transaction button clicks
    view.getAddTransactionBtn().addActionListener(e -> {
      // Get transaction data from view
      double amount = view.getAmountField();
      String category = view.getCategoryField();
      
      // Call controller to add transaction
      boolean added = controller.addTransaction(amount, category);
      
      if (!added) {
    	  view.showDialog("Invalid amount or category entered");
      }
    });

      // Add action listener to the "Apply Category Filter" button
    view.addApplyCategoryFilterListener(e -> {
      try{
      String categoryFilterInput = view.getCategoryFilterInput();
      CategoryFilter categoryFilter = new CategoryFilter(categoryFilterInput);
      if (categoryFilterInput != null) {
          // controller.applyCategoryFilter(categoryFilterInput);
          controller.setFilter(categoryFilter);
          boolean filterStatus = controller.applyFilter();
          if(!filterStatus) {
        	  view.showDialog("No filter applied");
          }
      }
     }catch(IllegalArgumentException exception) {
    	 view.showDialog(exception.getMessage());
   }});


    // Add action listener to the "Apply Amount Filter" button
    view.addApplyAmountFilterListener(e -> {
      try{
      double amountFilterInput = view.getAmountFilterInput();
      AmountFilter amountFilter = new AmountFilter(amountFilterInput);
      if (amountFilterInput != 0.0) {
          controller.setFilter(amountFilter);
          boolean filterStatus = controller.applyFilter();
          if(!filterStatus) {
        	  view.showDialog("No filter applied");
          }
      }
    }catch(IllegalArgumentException exception) {
    	view.showDialog(exception.getMessage());
   }});


    // Add action listener to the "Undo" button
    view.addUndoButtonListener(e -> {
      int selectedRowIndex = view.getSelectedRowIndex();
      boolean undoStatus = controller.undoTransaction(selectedRowIndex);
      if (undoStatus == false) {
    	  view.showDialog("Please select a transaction to undo");
      }
    });

  }
}
