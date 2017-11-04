package kz.rzaripov.ps_client;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kz.rzaripov.ps_client.json_class.ps_error;
import kz.rzaripov.ps_client.json_class.ps_get_domain_list;
import kz.rzaripov.ps_client.json_class.ps_get_product_list;
import kz.rzaripov.ps_client.listview_class.lv_domain_list;
import kz.rzaripov.ps_client.listview_class.lv_product_list;

public class ProductActivity extends AppCompatActivity {

    private String ps_user = "";
    private String ps_pswd = "";

    class AsyncRequest extends AsyncTask<List<String>, Integer, List<String>> {

        @Override
        protected List<String> doInBackground(List<String>... arg) {
            PSApi ps = new PSApi(ps_user, ps_pswd);
            List<String> results;
            results = new ArrayList<>();
            results.add(ps.requestTo(ps.getURL(arg[0].get(0))));
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

            ps_get_product_list js2 = new Gson().fromJson(s, ps_get_product_list.class);

            ListView product_list = (ListView) findViewById(R.id.product_list);
            product_list.setAdapter(new lv_product_list(ProductActivity.this, js2.answer.products));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ps_user = getIntent().getStringExtra("login");
        ps_pswd = getIntent().getStringExtra("pswd");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getString(R.string.my_products));

        List<String> list = new ArrayList<>();
        list.add(0, "get-product-list");
        new AsyncRequest().execute(list);
    }

    public Boolean isOK(String json) {
        return json.contains("\"success\"");
    }

}
