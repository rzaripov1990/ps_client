package kz.rzaripov.ps_client.listview_class;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kz.rzaripov.ps_client.R;
import kz.rzaripov.ps_client.json_class.get_products_list_answer_products;

/**
 * Created by ZuBy on 31.10.2017.
 */

public class lv_product_list extends ArrayAdapter<get_products_list_answer_products> {
    private final Activity context;
    private List<get_products_list_answer_products> products;

    public lv_product_list(Activity context, List<get_products_list_answer_products> products) {
        super(context, R.layout.product_list, products);
        this.context = context;
        this.products = products;
    }

    public Spanned stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    // Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
   public static class ViewHolder {
        public TextView nameView;
        public TextView regdateView;
        public TextView statusView;
        public TextView descriptionView;
        public TextView nextinvoicedateView;
        public TextView billingcycleView;
        public TextView amountView;
        public String Id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder буферизирует оценку различных полей шаблона элемента

        ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.product_list, null, true);
            holder = new lv_product_list.ViewHolder();
            holder.nameView = rowView.findViewById(R.id.product_name);
            holder.regdateView = rowView.findViewById(R.id.product_regdate);
            holder.statusView = rowView.findViewById(R.id.product_status);
            holder.descriptionView = rowView.findViewById(R.id.product_description);
            holder.nextinvoicedateView = rowView.findViewById(R.id.product_nextinvoicedate);
            holder.billingcycleView = rowView.findViewById(R.id.product_billingcycle);
            holder.amountView = rowView.findViewById(R.id.product_amount);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.nameView.setText(products.get(position).domain);
        holder.regdateView.setText("Регистрация: " + products.get(position).regdate);
        holder.statusView.setText("Статус: " +products.get(position).status);
        holder.nextinvoicedateView.setText("Следующая оплата: " +products.get(position).nextinvoicedate);
        holder.amountView.setText("Сумма: " + String.valueOf(products.get(position).amount));
        holder.descriptionView.setText(stripHtml(products.get(position).description));
        holder.billingcycleView.setText("Метод оплаты: " + products.get(position).billingcycle);
        holder.Id = products.get(position).id ;

        return rowView;
    }
}
