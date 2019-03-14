package org.twodee.connect440

import android.Manifest
import android.app.AlertDialog
import android.app.ListActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import java.net.URLEncoder
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.os.Build



class MainActivity : ListActivity() {

  private val friends = arrayOf<Friend>(
    Friend(
      "Chris Johnson",
      "Eau Claire, WI",
      "foo@canterbury.ac.nz",
      "123456789",
      "UU9023432"
    )
  )

  private val options = arrayOf("Map", "Email", "Text", "Call", "Slack")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    listAdapter = ArrayAdapter<Friend>(this, android.R.layout.simple_list_item_1, friends)

    val permissions = arrayOf(Manifest.permission.CALL_PHONE)
    if (!hasPermissions(permissions)) {
      requestPermissions(permissions, 1)
    }
  }

  fun hasPermissions(permissions: Array<String>): Boolean {
    return permissions.all { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
  }

  private fun dispatchAction(optionId: Int, friend: Friend) {
    when (optionId) {
      0 -> {
        val uri = Uri.parse("geo:0,0?q=${URLEncoder.encode(friend.home, "UTF-8")}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
      }
      1 -> {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, friend.email)
        startActivity(intent)
      }
      2 -> {
        val uri = Uri.parse("smsto:${friend.phone}")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(intent);
      }
      3 -> {
        val uri = Uri.parse("tel:${friend.phone}")
//        val intent = Intent(Intent.ACTION_DIAL, uri)
        val intent = Intent(Intent.ACTION_CALL, uri)
        startActivity(intent);
      }
      4 -> {
        val uri = Uri.parse("slack://user?team=TG6L2SPND&id=${friend.slackId}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent);
      }
    }
  }

  override fun onListItemClick(l: ListView?, v: View?, position: Int, friendId: Long) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("What action?")
    builder.setItems(options) { _, optionId ->
      dispatchAction(optionId, friends[friendId.toInt()])
    }
    builder.show()
  }
}
