import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.plantkeeper.R
import com.example.plantkeeper.models.Plant
import com.squareup.picasso.Picasso
import java.net.URL


class ProfilePostAdapter(
    private val context: Context,
    private val plants: Array<Plant>
) : BaseAdapter() {
    private var layourInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textview: TextView


    override fun getCount(): Int {
        return plants.count()
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (layourInflater == null) {
            layourInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layourInflater!!.inflate(R.layout.profile_row_item, parent, false)
        }
        imageView = convertView!!.findViewById(R.id.imageView)
        textview = convertView!!.findViewById(R.id.textView)
        textview.text = plants[position].name

        Picasso.get().load(plants[position].image).into(imageView);

        return convertView
    }
}