package org.capps.servlet;

import jakarta.servlet.annotation.WebServlet;
import org.capps.entity.Tag;
import org.capps.service.TagService;
import org.capps.service.implementation.TagServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/tags")
public class TagServlet extends HttpServlet {

    private TagService tagService;

    @Override
    public void init() throws ServletException {
        super.init();
        tagService = new TagServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tag> tags = tagService.getAllTags();
        request.setAttribute("tags", tags);
        request.getRequestDispatcher("/views/tags.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("DELETE".equalsIgnoreCase(method)) {
            handleDelete(request, response);
        } else if ("PUT".equalsIgnoreCase(method)) {
            handlePut(request, response);
        } else {
            Tag tag = createTagFromRequest(request, response);
            if (tag == null) return;

            try {
                tagService.addTag(tag);
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.sendRedirect(request.getContextPath() + "/tags");
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to add tag.");
            }
        }
    }

    private void handlePut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tag ID");
            return;
        }

        Tag tag = createTagFromRequest(request, response);
        if (tag == null) return;

        tag.setId(id);

        try {
            tagService.updateTag(tag);
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + "/tags");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while updating the tag.");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            tagService.deleteTag(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.sendRedirect(request.getContextPath() + "/tags"); // Redirect to the tags page
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid tag ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while deleting the tag.");
        }
    }

    private Tag createTagFromRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");

        // Validate parameters
        if (name == null || name.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tag name is required.");
            return null;
        }

        return new Tag(name);
    }

}
