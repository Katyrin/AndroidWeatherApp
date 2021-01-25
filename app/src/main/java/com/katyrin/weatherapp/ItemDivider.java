package com.katyrin.weatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDivider extends DividerItemDecoration {
   private final Drawable divider;

   public ItemDivider(Context context, int orientation, Drawable drawable) {
      super(context, orientation);
      this.divider = drawable;
   }

   @Override
   public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
                          @NonNull RecyclerView.State state) {
      super.onDrawOver(c, parent, state);

      int left = parent.getPaddingLeft();
      int right = parent.getWidth() - parent.getPaddingRight();

      for (int i = 0; i < parent.getChildCount() - 1; ++i) {
         View item = parent.getChildAt(i);

         int top = item.getBottom() + ((RecyclerView.LayoutParams)
            item.getLayoutParams()).bottomMargin;
         int bottom = top + divider.getIntrinsicHeight();

         divider.setBounds(left, top, right, bottom);
         divider.draw(c);
      }
   }
}