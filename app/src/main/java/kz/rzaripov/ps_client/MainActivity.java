package kz.rzaripov.ps_client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import kz.rzaripov.ps_client.json_class.ps_error;
import kz.rzaripov.ps_client.json_class.ps_get_balance;
import kz.rzaripov.ps_client.json_class.ps_get_profile_data;

public class MainActivity extends AppCompatActivity {

    private String ps_user = "PS_API_LOGIN";
    private String ps_pswd = "PS_API_PASS";

    class AsyncRequest extends AsyncTask<List<String>, Integer, List<String>> {

        @Override
        protected List<String> doInBackground(List<String>... arg) {

            PSApi ps = new PSApi(ps_user, ps_pswd);
            List<String> results;
            results = new ArrayList<>();
            results.add(ps.requestTo(ps.getURL(arg[0].get(0))));
            results.add(ps.requestTo(ps.getURL(arg[0].get(1))));
            return results;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);

            String s = list.get(0);
            if (!isOK(s)) {
                ps_error js = new Gson().fromJson(s, ps_error.class);
                Toast.makeText(getApplicationContext(), js.error_text, Toast.LENGTH_LONG).show();
                return;
            }

            ps_get_profile_data js = new Gson().fromJson(s, ps_get_profile_data.class);

            RelativeLayout content = (RelativeLayout) findViewById(R.id.content);
            RelativeLayout client = (RelativeLayout) findViewById(R.id.activity_client);


            content.removeView(client);
            LayoutInflater inflater = getLayoutInflater();
            inflater.inflate(R.layout.activity_client, content);

            TextView fio = (TextView) findViewById(R.id.fio);
            TextView company = (TextView) findViewById(R.id.company);
            TextView address = (TextView) findViewById(R.id.address);
            TextView email = (TextView) findViewById(R.id.email);
            TextView phone = (TextView) findViewById(R.id.phone);

            String f_name = js.answer[0].lastname + " " + js.answer[0].firstname;
            fio.setText(f_name.trim());
            company.setText(js.answer[0].companyname.isEmpty() ? "..." : js.answer[0].companyname);
            String adrs = js.answer[0].city + " " + js.answer[0].state;
            address.setText(adrs.trim());
            email.setText(js.answer[0].email);
            phone.setText(js.answer[0].phonenumber);

            s = list.get(1);
            if (!isOK(s)) {
                ps_error js1 = new Gson().fromJson(s, ps_error.class);
                Toast.makeText(getApplicationContext(), js1.error_text, Toast.LENGTH_LONG).show();
                return;
            }

            ps_get_balance js1 = new Gson().fromJson(s, ps_get_balance.class);

            TextView prepay = (TextView) findViewById(R.id.prepay);
            TextView credit = (TextView) findViewById(R.id.creadit);

            prepay.setText( Float.toString(js1.answer.prepay));
            credit.setText( Float.toString(js1.answer.credit));

            CardView cardDomains = (CardView) findViewById(R.id.cardDomains);
            cardDomains.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DomainActivity.class);
                    intent.putExtra("login", ps_user);
                    intent.putExtra("pswd", ps_pswd);
                    startActivity(intent);
                }
            });

            CardView cardProducts = (CardView) findViewById(R.id.cardProducts);
            cardProducts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                    intent.putExtra("login", ps_user);
                    intent.putExtra("pswd", ps_pswd);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> list = new ArrayList<>();
        list.add(0, "get-profile-data");
        list.add(1, "get-balance");
        new AsyncRequest().execute(list);
    }


    public Boolean isOK(String json) {
        return json.contains("\"success\"");
    }
}
