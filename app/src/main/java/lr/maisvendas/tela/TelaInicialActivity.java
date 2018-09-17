package lr.maisvendas.tela;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import lr.maisvendas.R;

public class TelaInicialActivity extends BaseActivity {

    public static final String PARAM_USUARIO = "PARAM_USUARIO";
    private static final String TAG = "TelaInicialActivity";

    private PieChart pieChart;

    float itensGrafico[] = {1.2f, 3.5f };

    String descricao[] = {"Orçado", "Realizado"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tela_inicial);
        setDrawerLayoutId(R.id.activity_tela_inicial_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Mais Vendas");

        pieChart = (PieChart) findViewById(R.id.activity_tela_inicial_pieChart);

        pieChart.setUsePercentValues(true);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        for(int i=0;i < itensGrafico.length; i++){
            yValues.add(new PieEntry(itensGrafico[i], i));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Election Results");

        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0;i < descricao.length; i++) {
            xVals.add(descricao[i]);
        }

        PieData data = new PieData(dataSet);
        // In Percentage
        data.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(data);
        //pieChart.setDescription("This is Pie Chart");
        pieChart.setDrawHoleEnabled(true);
        //pieChart.setTransparentCircleRadius(58f);

        //pieChart.setHoleRadius(58f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        //pieChart.setOnChartValueSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuSearch().setVisible(false);
        return true;
    }
}
