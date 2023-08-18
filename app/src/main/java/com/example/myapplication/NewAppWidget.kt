package com.example.myapplication

import android.app.Service
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Timer
import java.util.TimerTask

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {

    @RequiresApi(Build.VERSION_CODES.O)
    val currentTime = LocalTime.now().toString()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
       super.onUpdate(context, appWidgetManager, appWidgetIds)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        // 启动Service
        super.onEnabled(context)
        context.startService(Intent(context, ClockService::class.java))
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context)
        context.stopService(Intent(context, ClockService::class.java))
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    currentTime: String
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appwidget_text, currentTime)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

class ClockService : Service() {
    lateinit var timer : Timer
    val sdf : SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                updataView()
            }
        },0,1000)
    }

    private fun updataView() {
        var time : String = sdf.format(Date())
        var rViews : RemoteViews = RemoteViews(packageName, R.layout.new_app_widget)
        rViews.setTextViewText(R.id.appwidget_text, time)
        var manager : AppWidgetManager = AppWidgetManager.getInstance(applicationContext)
        var cName : ComponentName = ComponentName(applicationContext, NewAppWidget::class.java)
        manager.updateAppWidget(cName, rViews)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}