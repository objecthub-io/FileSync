package io.objecthub.filesync.internal.engine.convert

import com.appjangle.api.Node
import io.objecthub.filesync.Converter
import io.objecthub.filesync.FileOperation
import io.objecthub.filesync.ItemMetadata
import io.objecthub.filesync.Metadata
import io.objecthub.filesync.NetworkOperation
import de.mxro.file.FileItem
import delight.async.AsyncCommon
import delight.async.callbacks.ValueCallback
import java.util.ArrayList
import java.util.List

import static extension delight.async.AsyncCommon.*

class ConverterCollection implements Converter {
	
	val List<Converter> converters;
	
	new() {
		this.converters = new ArrayList<Converter>
	}
	
	def addConverter(Converter converter) {
		this.converters.add(converter)
	}
	
	override worksOn(FileItem source) {
		
		var res = false;

		for (c: converters) {
			res = res || (c.worksOn(source))
		}

		res
	}
	
	override worksOn(Node node, ValueCallback<Boolean> cb) {
		
		AsyncCommon.forEach(converters, [c, itmcb |
			c.worksOn(node, itmcb)
		], cb.embed [res |
			cb.onSuccess(res.contains(true))
		])
		
	}
	
	def private findConverter(FileItem forFileItem, ValueCallback<Converter> cb) {
		AsyncCommon.forEach(converters, [c, itmcb |
			if (c.worksOn(forFileItem)) {
				itmcb.onSuccess(c)
			} else {
				itmcb.onSuccess(ConvertUtils.NO_VALUE)
			}
		], cb.embed [res |
			for (item : res) {
				if (item instanceof Converter) {
					cb.onSuccess(item)
					return;
				}
			}
			cb.onFailure(new Exception("Cannot find converter for "+forFileItem))
		])
	}
	
	
	def private findConverter(ItemMetadata forItem, ValueCallback<Converter> cb) {
		for (c:converters) {
			if (c.class.toString.equals(forItem.converter)) {
				cb.onSuccess(c)
				return
			}
		}
		throw new RuntimeException("Cannot find converter for ["+forItem+"].\n"+
			"  Required Converter: "+forItem.converter+"\n"+
			"  Defined Converters: "+converters
		)
	}
	
	def private findConverter(Node forNode, ValueCallback<Converter> cb) {
		AsyncCommon.forEach(converters, [c, itmcb |
			c.worksOn(forNode, itmcb.embed [res |
				if (res) {
					itmcb.onSuccess(c)
				} else {
					itmcb.onSuccess(ConvertUtils.NO_VALUE)
				}
			])
			
		], cb.embed [res |
			for (item : res) {
				if (item instanceof Converter) {
					//println("using conv "+item+ " for "+forNode)
					cb.onSuccess(item)
					return;
				}
			}
			cb.onFailure(new Exception("Cannot find converter for "+forNode))
		])
	}
	
	override createNodes(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {
		findConverter(source, cb.embed [ converter |
			converter.createNodes(metadata, source, cb)
		])
	}
	
	override update(Metadata metadata, FileItem source, ValueCallback<List<NetworkOperation>> cb) {
		findConverter(metadata.get(source.name), cb.embed [ converter |
			converter.update(metadata, source, cb)
		])
	}
	
	override deleteNodes(Metadata metadata, ItemMetadata cachedFile, ValueCallback<List<NetworkOperation>> cb) {
		findConverter(cachedFile, cb.embed [ converter |
			converter.deleteNodes(metadata, cachedFile, cb)
		])
	}
	
	override createFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {
		if (source.uri().endsWith( "Types")) {
			new FolderToNode().createFiles(folder, metadata, source, cb)
			return;
		}
		
		findConverter(source, cb.embed [ converter |
			converter.createFiles(folder, metadata, source, cb)
		])
	}
	
	override updateFiles(FileItem folder, Metadata metadata, Node source, ValueCallback<List<FileOperation>> cb) {
		
		findConverter(metadata.get(source), cb.embed [ converter |
			converter.updateFiles(folder, metadata, source, cb)
		])
	}
	
	override removeFiles(FileItem folder, Metadata metadata, ItemMetadata item, ValueCallback<List<FileOperation>> cb) {
		findConverter(item, cb.embed [ converter |
			converter.removeFiles(folder, metadata, item, cb)
		])
	}
	
}