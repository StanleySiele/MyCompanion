package com.stan.app.mp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button connectButton;
    private EditText ipEditText, portEditText;
    private VncClient vncClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = findViewById(R.id.connect_button);
        ipEditText = findViewById(R.id.ip_edittext);
        portEditText = findViewById(R.id.port_edittext);

        vncClient = new VncClient();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipEditText.getText().toString();
                int port = Integer.parseInt(portEditText.getText().toString());

                vncClient.connect(ip, port);
            }
        });
    }
}
