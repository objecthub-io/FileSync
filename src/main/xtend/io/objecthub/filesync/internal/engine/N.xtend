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
		getTypeLink('html')
	}
	
	def  HTML_VALUE(Client client) {
		getType(client, 'html')
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
	
	
	
	def static COFFEESCRIPT() {
		getTypeLink('coffeescript')
	}
	
	def COFFEESCRIPT(Client session) {
		getType('coffeescript')
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