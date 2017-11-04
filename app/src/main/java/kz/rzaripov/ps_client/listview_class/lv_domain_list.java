package kz.rzaripov.ps_client.listview_class;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kz.rzaripov.ps_client.R;
import kz.rzaripov.ps_client.json_class.get_domains_list_answer;

/**
 * Created by ZuBy on 29.10.2017.
 */

public class lv_domain_list extends ArrayAdapter<get_domains_list_answer> {
    private final Activity context;
    private List<get_domains_list_answer> domains;

    public lv_domain_list(Activity context, List<get_domains_list_answer> domains) {
        super(context, R.layout.domain_list, domains);
        this.context = context;
        this.domains = domains;
    }

    // Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
    static class ViewHolder {
        public TextView domainView;
        public TextView dateView;
        public TextView statusView;
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
            rowView = inflater.inflate(R.layout.domain_list, null, true);
            holder = new ViewHolder();
            holder.domainView = rowView.findViewById(R.id.domain_name);
            holder.dateView = rowView.findViewById(R.id.domain_exdate);
            holder.statusView = rowView.findViewById(R.id.domain_status);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.domainView.setText(domains.get(position).domain);
        holder.dateView.setText("Продлен до: " + domains.get(position).expirydate);
        holder.statusView.setText("Статус: " + domains.get(position).status);

        return rowView;
    }
}
