<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.capps.entity.Task" %> <!-- Changez le chemin selon le bon package -->
<%@ page import="org.capps.entity.StatusTask" %>
<%@ page import="org.capps.entity.Tag" %>
<%@ page import="org.capps.entity.User" %>

<html>
<head>
  <title>Liste des Tâches</title>
  <link href="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
  <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
</head>
<body>
<jsp:include page="/layout/navbar.jsp" />
<div class="user-content mx-4 my-4">
  <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
    <div class="pb-4 bg-white dark:bg-gray-900">
      <button data-modal-target="add-task-modal" data-modal-toggle="add-task-modal" class="text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-500 dark:hover:bg-blue-600 dark:focus:ring-blue-800">
        Add Task
      </button>
    </div>
    <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
      <tr>
        <th scope="col" class="p-4">
          <input id="checkbox-all-search" type="checkbox" class="w-4 h-4">
        </th>
        <th scope="col" class="px-6 py-3">Titre</th>
        <th scope="col" class="px-6 py-3">Description</th>
        <th scope="col" class="px-6 py-3">Date de début</th>
        <th scope="col" class="px-6 py-3">Date de fin</th>
        <th class="border border-gray-400 p-2">Utilisateur</th>
        <th class="border border-gray-400 p-2">Tags</th>
        <th scope="col" class="px-6 py-3">Statut</th>
        <th scope="col" class="px-6 py-3">Action</th>
      </tr>
      </thead>
      <tbody>
      <%
        List<Task> tasks = (List<Task>) request.getAttribute("tasks"); // Récupérer la liste des tâches
        if (tasks != null) {
          for (Task task : tasks) {
      %>
      <tr class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
        <td class="w-4 p-4">
          <!-- ID unique pour chaque tâche -->
          <input id="checkbox-task-<%= task.getId() %>" type="checkbox" class="w-4 h-4">
        </td>
        <td class="px-6 py-4"><%= task.getTitle() %></td>
        <td class="px-6 py-4"><%= task.getDescription() %></td>
        <td class="px-6 py-4"><%= task.getStartDate() %></td>
        <td class="px-6 py-4"><%= task.getEndDate() %></td>
        <td class="border border-gray-400 p-2">
          <%= task.getUser().getUsername() %> - <%= task.getUser().getEmail() %>
        </td>
        <td class="border border-gray-400 p-2">
          <ul>
            <%
              for (Tag tag : task.getTags()) {
            %>
            <li><%= tag.getName() %></li>
            <%
              }
            %>
          </ul>
        </td>
        <td class="border border-gray-400 p-2">
          <form action="tasks" method="POST" id="statusForm_<%= task.getId() %>">
            <input type="hidden" name="taskId" value="<%= task.getId() %>" />
            <input type="hidden" name="_method" value="UPDATE_STATUS">
            <select name="status" required onchange="document.getElementById('statusForm_<%= task.getId() %>').submit()">
              <option value="PENDING" <%= task.getStatus() == StatusTask.PENDING ? "selected" : "" %>>PENDING</option>
              <option value="IN_PROGRESS" <%= task.getStatus() == StatusTask.IN_PROGRESS ? "selected" : "" %>>IN_PROGRESS</option>
              <option value="COMPLETED" <%= task.getStatus() == StatusTask.COMPLETED ? "selected" : "" %>>COMPLETED</option>
              <option value="CANCELLED" <%= task.getStatus() == StatusTask.CANCELLED ? "selected" : "" %>>CANCELLED</option>
            </select>
          </form>
        </td>
        <td class="px-6 py-4">
          <!-- Modal pour la mise à jour de la tâche -->
          <a href="#" data-modal-target="update-task-modal-<%= task.getId() %>" data-modal-toggle="update-task-modal-<%= task.getId() %>" class="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">
            Update
          </a>

          <form action="tasks" method="post" style="display:inline;">
            <input type="hidden" name="id" value="<%= task.getId() %>">
            <input type="hidden" name="_method" value="DELETE">
            <button type="submit" class="text-white bg-gradient-to-r from-red-400 via-red-500 to-red-600 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-red-300 dark:focus:ring-red-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">
              Delete
            </button>
          </form>

          <form action="userTokenServlet" method="POST" style="display:inline;">
            <input type="hidden" name="_method" value="PUT">
            <input type="hidden" name="userId" value="1" />
            <input type="hidden" name="taskId" value="<%= task.getId() %>" />
            <button type="submit" class="text-white bg-gradient-to-r from-pink-400 via-pink-500 to-pink-600 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-pink-300 dark:focus:ring-pink-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">
              Delete the task
            </button>
          </form>



        </td>
      </tr>
      <!-- Modal pour la mise à jour de chaque tâche -->
      <div id="update-task-modal-<%= task.getId() %>" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
        <div class="relative p-4 w-full max-w-md max-h-full">
          <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
            <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600">
              <h3 class="text-xl font-semibold text-gray-900 dark:text-white">
                Update Task - <%= task.getTitle() %>
              </h3>
              <button type="button" class="text-gray-400" data-modal-hide="update-task-modal-<%= task.getId() %>">
                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                  <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                </svg>
                <span class="sr-only">Close modal</span>
              </button>
            </div>
            <div class="p-4 md:p-5">
              <form action="tasks" method="POST" class="space-y-4">
                <input type="hidden" name="id" value="<%= task.getId() %>">
                <input type="hidden" name="_method" value="PUT">
                <div>
                  <label for="title-<%= task.getId() %>" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Title:</label>
                  <input type="text" id="title-<%= task.getId() %>" name="title" value="<%= task.getTitle() %>" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"/>
                </div>
                <div>
                  <label for="description-<%= task.getId() %>" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Description:</label>
                  <textarea id="description-<%= task.getId() %>" name="description" rows="4" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"><%= task.getDescription() %></textarea>
                </div>
                <div>
                  <label for="start_date-<%= task.getId() %>" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
                    Start Date:
                  </label>
                  <input type="date" id="start_date-<%= task.getId() %>" name="start_date" value="<%= task.getStartDate() %>" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white" onchange="updateEndDateMin('<%= task.getId() %>')" /> <!-- Appel dynamique de la fonction JS -->
                </div>

                <div>
                  <label for="end_date-<%= task.getId() %>" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
                    End Date:
                  </label>
                  <input type="date" id="end_date-<%= task.getId() %>" name="end_date" value="<%= task.getEndDate() %>" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"/>
                </div>
                <div>
                  <label for="status-<%= task.getId() %>" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Status:</label>
                  <select id="status-<%= task.getId() %>" name="status" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white">
                    <option value="PENDING" <%= task.getStatus() == StatusTask.PENDING ? "selected" : "" %>>Pending</option>
                    <option value="IN_PROGRESS" <%= task.getStatus() == StatusTask.IN_PROGRESS ? "selected" : "" %>>In Progress</option>
                    <option value="COMPLETED" <%= task.getStatus() == StatusTask.COMPLETED ? "selected" : "" %>>Completed</option>
                    <option value="CANCELLED" <%= task.getStatus() == StatusTask.CANCELLED ? "selected" : "" %>>Cancelled</option>
                  </select>
                </div>
                <div class="mb-4">
                  <label for="user_id" class="block text-sm font-medium">Utilisateur</label>
                  <select id="user_id" name="user_id" class="w-full px-3 py-2 border rounded-lg">
                    <%
                      List<User> users = (List<User>) request.getAttribute("users");
                      if(users != null){
                        for(User user : users){
                    %>
                    <option value="<%=user.getId()%>" <%= task.getUser().equals(user.getId()) ? "selected" : "" %>> <%= user.getUsername() %> </option>
                    <%
                        }}
                    %>
                  </select>
                </div>

                <div class="mb-4">
                  <label for="tag_id" class="block text-sm font-medium">Tags</label>
                  <select id="tag_id" name="tag_id" multiple="multiple" class="w-full px-3 py-2 border rounded-lg">
                    <%
                      List<Tag> tags = (List<Tag>) request.getAttribute("tags");
                      if(tags != null){
                        for(Tag tag : tags){
                    %>
                    <option value="<%=tag.getId()%>" <%= task.getTags().contains(tag) ? "selected" : "" %>> <%= tag.getName() %> </option>
                    <%
                        }}
                    %>
                  </select>
                </div>
                <div class="flex justify-end">
                  <button type="submit" class="text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-500 dark:hover:bg-blue-600 dark:focus:ring-blue-800">
                    Update
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      <%
          }
        }
      %>
      </tbody>
    </table>
  </div>

  <!-- Modal for Adding Task -->
  <div id="add-task-modal" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
    <div class="relative p-4 w-full max-w-md max-h-full">
      <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
        <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600">
          <h3 class="text-xl font-semibold text-gray-900 dark:text-white">
            Add New Task
          </h3>
          <button type="button" class="text-gray-400" data-modal-hide="add-task-modal">
            <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
              <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
            </svg>
            <span class="sr-only">Close modal</span>
          </button>
        </div>
        <div class="p-4 md:p-5">
          <form action="tasks" method="POST" class="space-y-4">
            <div>
              <label for="title" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Title:</label>
              <input type="text" id="title" name="title" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"/>
            </div>
            <div>
              <label for="description" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Description:</label>
              <textarea id="description" name="description" rows="4" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"></textarea>
            </div>
            <div>
              <label for="start_date" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
                Start Date:
              </label>
              <input type="date" id="start_date" name="start_date" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white" onchange="updateEndDateMin()"/>
            </div>

            <div>
              <label for="end_date" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">
                End Date:
              </label>
              <input type="date" id="end_date" name="end_date" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"/>
            </div>
            <div>
              <label for="status" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Status:</label>
              <select id="status" name="status" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white">
                <option value="PENDING">Pending</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="COMPLETED">Completed</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
            <div class="mb-4">
              <label for="users" class="block text-sm font-medium">Tags</label>
              <select id="users" name="user_id" class="w-full px-3 py-2 border rounded-lg">
                <%
                  List<User> users = (List<User>) request.getAttribute("users");
                  if(users != null){
                    for(User user : users){
                %>
                <option value="<%=user.getId()%>"> <%= user.getUsername() %> </option>
                <%
                    }}
                %>
              </select>
            </div>
            <div class="mb-4">
              <label for="tags" class="block text-sm font-medium">Tags</label>
              <select id="tags" name="tag_id" multiple="multiple" class="w-full px-3 py-2 border rounded-lg">
                <%
                  List<Tag> tags = (List<Tag>) request.getAttribute("tags");
                  if(tags != null){
                    for(Tag tag : tags){
                %>
                <option value="<%=tag.getId()%>"> <%= tag.getName() %> </option>
                <%
                    }}
                %>
              </select>
            </div>
            <div class="flex justify-end">
              <button type="submit" class="text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-500 dark:hover:bg-blue-600 dark:focus:ring-blue-800">
                Add
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
<script>
  function updateEndDateMin() {
    const startDate = document.getElementById('start_date').value;
    const endDate = document.getElementById('end_date');

    if (startDate) {
      endDate.min = startDate;
    }
  }
</script>
</body>
</html>
