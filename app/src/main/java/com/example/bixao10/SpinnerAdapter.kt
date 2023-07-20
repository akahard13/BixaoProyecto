import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.bixao10.R

class SpinnerAdapter(context: Context, items: Array<String>) :
    ArrayAdapter<String>(context, R.layout.spinner_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        // Personaliza el texto según tus necesidades
        textView.setTextColor(ContextCompat.getColor(context, R.color.whitey))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.regular_tommy))
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        // Personaliza el texto según tus necesidades
        textView.setTextColor(ContextCompat.getColor(context, R.color.whitey))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.regular_tommy))
        return view
    }
}
