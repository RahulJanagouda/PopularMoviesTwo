package com.rahuljanagouda.popularmoviestwo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.TypedValue;

import com.rahuljanagouda.popularmoviestwo.database.MoviesContract;

/**
 * Created by rahuljanagouda on 15/08/16.
 */
public class General {
    public static int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static boolean isMovieFavorite(Context mContext, String movieIdForSearch){

        String[] mProjection =
                {
                        MoviesContract.MoviesEntry.MOVIE_ID,    // Contract class constant for the _ID column name
                };

        String mSelectionClause = MoviesContract.MoviesEntry.MOVIE_ID + " = ?";
        String[] mSelectionArgs = new String[1];
        mSelectionArgs[0] = movieIdForSearch;

        Cursor mCursor = mContext.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null
                );

        if (mCursor != null && mCursor.getCount() >0){
            return true;
        }

        return false;
    }
}
