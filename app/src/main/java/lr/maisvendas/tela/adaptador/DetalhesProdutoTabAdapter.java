package lr.maisvendas.tela.adaptador;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetalhesProdutoTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> listFragments = new ArrayList<>();
    private List<String> listFragmentsTitle = new ArrayList<>();

    public DetalhesProdutoTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment frag, String title) {
        this.listFragments.add(frag);
        this.listFragmentsTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return listFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listFragmentsTitle.get(position);
    }
}
