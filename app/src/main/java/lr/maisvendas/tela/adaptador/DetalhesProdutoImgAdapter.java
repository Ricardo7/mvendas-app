package lr.maisvendas.tela.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.utilitarios.Ferramentas;

public class DetalhesProdutoImgAdapter extends PagerAdapter {

    private static final String TAG = "DetalhesProdutoImgAdapter";

    private Context context;
    //private int[] imagens = new int[] { R.drawable.ic_filter, R.drawable.ic_date, R.drawable.ic_exit };
    private List<Imagem> imagens;

    public DetalhesProdutoImgAdapter(Context context, List<Imagem> imagens) {
        this.context = context;
        this.imagens = imagens;
    }

    @Override
    public int getCount() {
        //return imagens.length;
        return imagens.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup pager, int position, Object object) {
        ((ViewPager) pager).removeView((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup pager, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        Imagem imagem = imagens.get(position);

        if (imagem.getCaminho() != null) {
            File imgFile = new  File(imagem.getCaminho());

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            if (bitmap != null) {
                Bitmap reduzirBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);

                Ferramentas ferramentas = new Ferramentas();
                ferramentas.customLog(TAG,"getWidth: "+reduzirBitmap.getWidth());
                ferramentas.customLog(TAG,"getHeight: "+reduzirBitmap.getHeight());
                ferramentas.customLog(TAG,"getPixel: "+reduzirBitmap.getPixel(1,1));


                imageView.setImageBitmap(reduzirBitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                //imageView.setImageResource(imagens[position]);
                ((ViewPager) pager).addView(imageView, 0);
            }
        }


        return imageView;
    }
}