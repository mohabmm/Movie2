package data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nocom.movie2.R;

/**
 * {@link MovieCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class MovieCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link MovieCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.favorites, parent, false);


    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: Fill out this metho// Find fields to populate in inflated template
        TextView tvBody = (TextView) view.findViewById(R.id.textView);
        TextView tvPriority = (TextView) view.findViewById(R.id.textView2);
        // Extract properties from cursor
        int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
        int TitleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);

        String Columnid = cursor.getString(idColumnIndex);
        String columnTitle = cursor.getString(TitleColumnIndex);
        // Populate fields with extracted properties
        tvBody.setText(Columnid);
        tvPriority.setText(columnTitle);
    }
}