package com.threepbears.quiktrak;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.Date;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText dateEditText;
    private EditText amountEditText;
    private Spinner categorySpinner;
    private EditText noteEditText;
    private Button addTransButton;

    private TransactionRoomDatabase transDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transDB = TransactionRoomDatabase.getDatabase(this);

        dateEditText = findViewById(R.id.dateEditText);
        amountEditText = findViewById(R.id.amountEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        noteEditText = findViewById(R.id.noteEditText);
        addTransButton = findViewById(R.id.addTransactionButton);

        // Disabling manual editing of the date field, must use the popup calendar
        dateEditText.setShowSoftInputOnFocus(false);
        dateEditText.setInputType(InputType.TYPE_NULL);
        dateEditText.setFocusable(false);

        Calendar cal = Calendar.getInstance();
        dateEditText.setText(DateFormatter.intsToStringDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePicker = new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final String date = DateFormatter.intsToStringDate(day, month+1, year);
                        dateEditText.setText(date);
                    }
                }, year, month, day);

                datePicker.show();
            }
        });

        addTransButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTransaction();
                finish();
            }
        });

        amountEditText.requestFocus();
        amountEditText.addTextChangedListener(new CurrencyEditTextWatcher(amountEditText));
        amountEditText.setText("0");
    }

    private void addNewTransaction() {
        // Dividing the current time by 1000 to make it fit into an int and using that as
        // the id of the transaction
        Transaction newTrans = new Transaction((int) (new Date().getTime() / 1000),
                DateFormatter.stringToDate(dateEditText.getText().toString()),
                Integer.parseInt(amountEditText.getText().toString().replaceAll("[$,.]", "")),
                categorySpinner.getSelectedItem().toString(),
                noteEditText.getText().toString());

        writeTransactionToDB(newTrans);
    }

    private void writeTransactionToDB(Transaction newTrans) {
        TransactionDao transDao = transDB.transactionDao();

        transDao.insert(newTrans);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addNewTransaction();
        finish();
        return true;
    }
}
