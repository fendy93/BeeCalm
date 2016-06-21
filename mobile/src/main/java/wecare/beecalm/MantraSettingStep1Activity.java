package wecare.beecalm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Zhuosi on 4/20/16.
 */
public class MantraSettingStep1Activity extends AppCompatActivity {

    private DataContainer dc;
    private String content;
    private EditText editText;
    private String position = null;
    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantra_setting_step1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mantra Settings");

        Intent intent = getIntent();
        dc = DataContainer.getInstance();
        editText = (EditText) findViewById(R.id.textInput);

        position = intent.getStringExtra("position");
        if(position != null){
            content = dc.getMantraContent(position);
            editText.setText(content);
        }

        button = (Button) findViewById(R.id.nextButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editText.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(), "must specify the content first", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(MantraSettingStep1Activity.this, MantraSettingStep2Activity.class);
                    if(position != null){
                        dc.updateMantraList(Integer.parseInt(position), content);
                    }else{
                        position =  dc.insertNewMantra(content);
                    }
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mantras Setting");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.side_bar, menu);
        super.onCreateOptionsMenu(menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        super.onOptionsItemSelected(item);
        this.closeOptionsMenu();
        Intent intent_back = null;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                content = editText.getText().toString();
                if(position != null && content.length() != 0) {
                    dc.updateMantraList(Integer.parseInt(position), content);
                    dc.writeConfigure(getApplicationContext());
                    System.out.println("start transmitting to the watch");
                    Intent intent = new Intent(MantraSettingStep1Activity.this, PhoneToWatchService.class);
                    startService(intent);
                    System.out.println("finished transmitting to the watch");
                }else if( content.length() != 0) {
                    dc.insertNewMantra(content);
                    dc.writeConfigure(getApplicationContext());
                    System.out.println("start transmitting to the watch");
                    Intent intent = new Intent(MantraSettingStep1Activity.this, PhoneToWatchService.class);
                    startService(intent);
                    System.out.println("finished transmitting to the watch");
                }
                intent_back = new Intent(this, MantraSettingsActivity.class);
                break;
        }
        startActivity(intent_back);
        return true;
    }

}
