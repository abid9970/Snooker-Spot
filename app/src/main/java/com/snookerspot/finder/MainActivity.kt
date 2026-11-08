package com.snookerspot.finder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private var geoCallback: GeolocationPermissions.Callback? = null
    private var geoOrigin: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setGeolocationEnabled(true)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url ?: return false
                if (url.scheme == "https" && url.host == "appassets.androidplatform.net") return false
                return try { startActivity(Intent(Intent.ACTION_VIEW, url)); true } catch (_: Exception) { true }
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                geoOrigin = origin
                geoCallback = callback
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    callback?.invoke(origin, true, false)
                } else requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
            }
        }
        // Local HTTPS-equivalent origin for geolocation; no public web hosting required.
        val loader = androidx.webkit.WebViewAssetLoader.Builder().addPathHandler("/assets/", androidx.webkit.WebViewAssetLoader.AssetsPathHandler(this)).build()
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): android.webkit.WebResourceResponse? =
                request?.url?.let { loader.shouldInterceptRequest(it) }
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url ?: return false
                if (url.host == "appassets.androidplatform.net") return false
                return try { startActivity(Intent(Intent.ACTION_VIEW, url)); true } catch (_: Exception) { true }
            }
        }
        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            geoCallback?.invoke(geoOrigin, grantResults.any { it == PackageManager.PERMISSION_GRANTED }, false)
            geoCallback = null
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() { if (webView.canGoBack()) webView.goBack() else super.onBackPressed() }
}
