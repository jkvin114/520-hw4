package controller;

import view.ExpenseTrackerView;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.TransactionFilter;

public class ExpenseTrackerController {
  
  private ExpenseTrackerModel model;
  /** 
   * The Controller is applying the Strategy design pattern.
   * This is the has-a relationship with the Strategy class 
   * being used in the applyFilter method.
   */
  private TransactionFilter filter;

  public ExpenseTrackerController(ExpenseTrackerModel model) {
    this.model = model;
    // For the MVC architecture pattern, the Observer design pattern is being
    // used to update the View after manipulating the Model.
    //hw4 UPDATE: removed view dependency from controller. the view is registered to the model before 
    // model gets injected into controller.
  }

  public void setFilter(TransactionFilter filter) {
    // Sets the Strategy class being used in the applyFilter method.
    this.filter = filter;
  }

  public boolean addTransaction(double amount, String category) {
    if (!InputValidation.isValidAmount(amount)) {
      return false;
    }
    if (!InputValidation.isValidCategory(category)) {
      return false;
    }
    
    Transaction t = new Transaction(amount, category);
    model.addTransaction(t);
    
    return true;
  }

  /**
   * 
   * @return true if filter successfully applied. false on invalid input or null filter.
   */
  public boolean applyFilter() {
    //null check for filter
    if(filter!=null){
      // Use the Strategy class to perform the desired filtering
      List<Transaction> transactions = model.getTransactions();
      List<Transaction> filteredTransactions = filter.filter(transactions);
      List<Integer> rowIndexes = new ArrayList<>();
      for (Transaction t : filteredTransactions) {
        int rowIndex = transactions.indexOf(t);
        if (rowIndex != -1) {
          rowIndexes.add(rowIndex);
        }
      }
      model.setMatchedFilterIndices(rowIndexes);
      return true;
    }
    else{
    	return false;
      }

  }

  //for undoing any selected transaction
  public boolean undoTransaction(int rowIndex) {
    if (rowIndex >= 0 && rowIndex < model.getTransactions().size()) {
      Transaction removedTransaction = model.getTransactions().get(rowIndex);
      model.removeTransaction(removedTransaction);
      // The undo was allowed.
      return true;
    }

    // The undo was disallowed.
    return false;
  }    
}
