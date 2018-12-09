package com.example.megaloma.societywidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlin.random.Random

/**
 * Implementation of App Widget functionality.
 */
class SocietyWidget : AppWidgetProvider() {

    private var outText = "86400秒"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // 複数widgetが作成された場合に呼ばれる
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // 最初にwidgetが作成された時に実行
    }

    override fun onDisabled(context: Context) {
        // Homeに複数のwidgetがある場合最後に呼ばれる
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val views = RemoteViews(context.packageName, R.layout.society_widget)

            // 自身でBroadCastを受け取るためのアクションを設定
            val intent = Intent("android.appwidget.action.APPWIDGET_UPDATE" )
            val pendingIntent = PendingIntent.getBroadcast(context,appWidgetId,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            // クリック時発火するように設定
            views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when(intent.action){
            "android.appwidget.action.APPWIDGET_UPDATE"  -> {

                //乱数に応じて出すテキストを変更
                val randomValue = Random.nextInt(SpeechConst.speechList.size)
                this.outText = SpeechConst.speechList[randomValue]

                val views = RemoteViews(context.packageName, R.layout.society_widget)
                views.setTextViewText(R.id.appwidget_text, outText)

                val appWidgetManager = AppWidgetManager.getInstance(context)
                val componentName = ComponentName(context, this::class.java)
                appWidgetManager.updateAppWidget(componentName, views)
            }
        }
    }
}

