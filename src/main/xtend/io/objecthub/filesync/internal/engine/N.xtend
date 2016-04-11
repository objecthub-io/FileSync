package io.objecthub.filesync.internal.engine

import com.appjangle.api.Client
import com.appjangle.api.Link

class N {
	
	static val ID = "100"
	
	def static String getTypeLink(String type) {
		"https://beta.objecthub.io/dev/~"+ID+"/~hub/hub/repo/~objects/~"+type+"/versions/~*0.node.html"	
	}
	
	def static Link getType(Client client, String type) {
		client.link(getTypeLink(type))
	}
	
	def static HTML_VALUE() {
		"https://admin1.linnk.it/types/v01/isHtmlValue"
	}
	
	def  HTML_VALUE(Client session) {
		session.link("https://admin1.linnk.it/types/v01/isHtmlValue")
	}
	
	def static TEMPLATE() {
		"https://u1.linnk.it/6wbnoq/Types/aTemplate"
	}
	
	def TEMPLATE(Client session) {
		 session.link(TEMPLATE)
	}
	
	def static TEXT_VALUE() {
		"https://u1.linnk.it/6wbnoq/Types/aTextValue"
	}
	
	def  TEXT_VALUE(Client session) {
		session.link(TEXT_VALUE)
	}
	
	def static LABEL() {
		"https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/shortLabel"
	}
	
	def LABEL(Client session) {
		session.link(LABEL)
	}
	
	def static LABEL2() {
		"https://u1.linnk.it/qc8sbw/usr/apps/textsync/upload/label"
	}
	
	def static LABEL3() {
		"https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/longLabel"
	}
	
	def static ICON() {
		"https://u1.linnk.it/qc8sbw/usr/apps/textsync/files/icon32"
	}
	
	def ICON(Client session) {
		 session.link(ICON)
	}
	
	def static COFFEESCRIPT() {
		"http://slicnet.com/mxrogm/mxrogm/data/stream/2014/1/10/n3"
	}
	
	def COFFEESCRIPT(Client session) {
		session.link(COFFEESCRIPT)
	}
	
	def static JAVASCRIPT() {
		"https://u1.linnk.it/qc8sbw/usr/apps/textsync/upload/java-script-document"
	}
	
	def JAVASCRIPT(Client session) {
		session.link(JAVASCRIPT)
	}
	
	def static CSS() {
		"https://u1.linnk.it/qc8sbw/usr/apps/textsync/upload/CSSDocument"
	}
	
	def  CSS(Client session) {
		session.link(CSS)
	}
	
	def static TYPE() {
		"http://slicnet.com/mxrogm/mxrogm/data/stream/2013/12/11/n5"
	}
	
	def TYPE(Client session) {
		session.link(TYPE)
	}
	
	def static RICHTEXT() {
		"http://slicnet.com/mxrogm/mxrogm/data/stream/2014/3/28/n3"
	}
	
	def RICHTEXT(Client session) {
		session.link(RICHTEXT)
	}
	
}