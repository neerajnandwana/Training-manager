package com.mgr.training.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.inject.Provider;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.joda.time.DateTime;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mgr.training.AppSession;
import com.mgr.training.data.Attachment;
import com.mgr.training.data.User;
import com.mgr.training.store.AttachmentStore;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("attachment")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@Singleton
public class AttachmentResource {
	private final Provider<AppSession> sessionProvider;
	private final Provider<HttpServletResponse> responseProvider;
	private final AttachmentStore attachmentStore;
	private final static Tika TIKA = new Tika();

	@Inject
	public AttachmentResource(Provider<AppSession> sessionProvider, 
			Provider<HttpServletResponse> responseProvider,
			AttachmentStore attachmentStore) {
		this.sessionProvider = sessionProvider;
		this.responseProvider = responseProvider;
		this.attachmentStore = attachmentStore;
		
	}

	@GET
	@Timed
	public Object all(@Context UriInfo ui) {
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		return attachmentStore.query(queryParams);
	}

	@GET
	@Timed
	@Path("{id}")
	public Object getById(@PathParam("id") String id) {
		return attachmentStore.findById(id);
	}

	@DELETE
	@Timed
	@Path("{id}")
	public Object deleteById(@PathParam("id") String id) {
		return attachmentStore.deleteById(id);
	}

	@GET
	@Timed
	@Path("{id}/file")
	public Object downloadById(@PathParam("id") String id) throws IOException {
		Attachment attachment = attachmentStore.findById(id);
		
		if(attachment == null){
			return Response.noContent().build();
		}

		HttpServletResponse response = responseProvider.get();
		byte[] bytes = attachment.getContent();
	    response.setContentType(attachment.getMimeType() + "; charset=UTF-8");
	    response.setContentLength(bytes.length);
	    response.setHeader("content-disposition", "attachment; filename = "+attachment.getName());
	    ServletOutputStream out = response.getOutputStream();
	    IOUtils.write(bytes, out);
	    IOUtils.closeQuietly(out);
	    return out;
	}

	@POST
	@Timed
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Object upload(
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("desc") String desc) throws IOException {
		User user = sessionProvider.get().getUser();
		String fileId = UUID.randomUUID().toString();
		String fileName = fileDetail.getFileName();
		String fileType = TIKA.detect(inputStream, fileName);
		byte[] data = IOUtils.toByteArray(inputStream);
	    IOUtils.closeQuietly(inputStream);	    
	    Attachment attachment = new Attachment();
	    attachment.setId(fileId);
	    attachment.setAuthor(user);
	    attachment.setName(fileName);
	    attachment.setDesc(desc);
	    attachment.setContent(data);        
	    attachment.setMimeType(fileType);
	    attachment.setCreated(DateTime.now().toDate());
		return attachmentStore.create(attachment);
	}
}
