package andersonrsoares.com.br.mlkit.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import andersonrsoares.com.br.mlkit.PojoApi
import andersonrsoares.com.br.mlkit.R
import andersonrsoares.com.br.mlkit.activity.BarCodeReaderActivity
import andersonrsoares.com.br.mlkit.activity.CardScannerActivity
import andersonrsoares.com.br.mlkit.activity.FaceDetectionActivity
import andersonrsoares.com.br.mlkit.activity.ImageLabelActivity
import kotlinx.android.synthetic.main.item_row_home.view.*

class HomeAdapter(private val apiList: List<PojoApi>) : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {

    private lateinit var context: Context

    class HomeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        val currItem = apiList[position]
        with(holder.itemView) {
            tViewApiName.text = currItem.title
            tViewApiDesc.text = currItem.desc
            iViewApi.setImageResource(currItem.imageId)
            cViewHome.setOnClickListener {
                when(currItem.id){
                    0 -> context.startActivity(Intent(context, ImageLabelActivity::class.java))
                    1 -> context.startActivity(Intent(context, CardScannerActivity::class.java))
                    2 -> context.startActivity(Intent(context, BarCodeReaderActivity::class.java))
                    3 -> Toast.makeText(context,"Work in Progress",Toast.LENGTH_SHORT).show()
                    4 -> Toast.makeText(context,"Work in Progress",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        context = parent.context
        return HomeHolder(LayoutInflater.from(context).inflate(R.layout.item_row_home, parent, false))
    }

    override fun getItemCount() = apiList.size
}