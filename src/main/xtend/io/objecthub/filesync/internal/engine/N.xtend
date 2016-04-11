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
		getTypeLink('plain-cs')
	}
	
	def COFFEESCRIPT(Client client) {
		getType(client, 'plain-cs')
	}
	
	def static JAVASCRIPT() {
		getTypeLink('javascript')
	}
	
	def JAVASCRIPT(Client client) {
		getType(client, 'javascript')
	}
	
	def static CSS() {
		getTypeLink('css')
	}
	
	def CSS(Client client) {
		getType(client, 'css')
	}
	
	def static ATTRIBUTE() {
		getTypeLink('attribute')
	}
	
	def ATTRIBUTE(Client client) {
		getType(client, 'attribute')
	}
	
	// TBD
	def static RICHTEXT() {
		"http://slicnet.com/mxrogm/mxrogm/data/stream/2014/3/28/n3"
	}
	
	def RICHTEXT(Client session) {
		session.link(RICHTEXT)
	}
	
}