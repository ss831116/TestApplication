package com.yuyu.android.wct.gallery.imageloader;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.gallery.bean.ImageFloder;
import com.yuyu.android.wct.gallery.utils.BasePopupWindowForListView;
import com.yuyu.android.wct.gallery.utils.CommonAdapter;
import com.yuyu.android.wct.gallery.utils.ViewHolder;

import java.util.List;

/**
 * Created by bernie.shi on 2016/4/13.
 */
public class ListImageDirPopupWindow extends BasePopupWindowForListView<ImageFloder>
{
    private ListView mListDir;

    public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFloder> datas, View convertView)
    {
        super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews()
    {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
                R.layout.list_dir_item)
        {
            @Override
            public void convert(ViewHolder helper, ImageFloder item)
            {
                helper.setText(R.id.id_dir_item_name, item.getName());
                helper.setImageByUrl(R.id.id_dir_item_image,
                        item.getFirstImagePath());
                helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
            }
        });
    }

    public interface OnImageDirSelected
    {
        void selected(ImageFloder floder);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected)
    {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents()
    {
        mListDir.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id)
            {

                if (mImageDirSelected != null)
                {
                    mImageDirSelected.selected(mDatas.get(position));
                }
            }
        });
    }

    @Override
    public void init()
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params)
    {
        // TODO Auto-generated method stub
    }

}
