<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.capps.entity.Tag" %> <!-- Change le chemin selon le bon package -->

<%
    if (session.getAttribute("name") == null){
        response.sendRedirect("authontification/login.jsp");
    }
%>
<html>
<head>
    <title>Liste des Utilisateurs</title>
    <link href="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.css" rel="stylesheet" />
</head>
<body>
<jsp:include page="/layout/navbar.jsp" />
<div class="user-content mx-4 my-4 ">
    <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
        <div class="pb-4 bg-white dark:bg-gray-900">
            <button data-modal-target="add-tag-modal" data-modal-toggle="add-tag-modal" class="text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-500 dark:hover:bg-blue-600 dark:focus:ring-blue-800">
                Add Tag
            </button>
        </div>
        <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
                <th scope="col" class="p-4">
                    <input id="checkbox-all-search" type="checkbox" class="w-4 h-4">
                </th>
                <th scope="col" class="px-6 py-3">name</th>
                <th scope="col" class="px-6 py-3">Action</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Tag> tags = (List<Tag>) request.getAttribute("tags"); // Récupérer la liste des tâches
                if (tags != null) {
                    for (Tag tag : tags) {
            %>
            <tr class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
                <td class="w-4 p-4">
                    <!-- Unique ID based on user ID -->
                    <input id="checkbox-user-<%= tag.getId() %>" type="checkbox" class="w-4 h-4">
                </td>
                <td class="px-6 py-4"><%= tag.getName() %></td>
                <td class="px-6 py-4">
                    <!-- Unique modal reference using user ID -->
                    <a href="#" data-modal-target="update-tag-modal-<%= tag.getId() %>" data-modal-toggle="update-tag-modal-<%= tag.getId() %>" class="text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-xs px-3 py-2 text-center dark:bg-blue-500 dark:hover:bg-blue-600 dark:focus:ring-blue-800 mr-2">
                        Update
                    </a>

                    <form action="tags" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= tag.getId() %>">
                        <input type="hidden" name="_method" value="DELETE">
                        <button type="submit" class="text-white bg-red-600 hover:bg-red-700 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-xs px-3 py-2 text-center dark:bg-red-500 dark:hover:bg-red-600 dark:focus:ring-red-800">
                            Delete
                        </button>
                    </form>

                </td>
            </tr>
            <!-- Modal for each user -->
            <div id="update-tag-modal-<%= tag.getId() %>" tabindex="-1" aria-hidden="true" class="hidden overflow-y-auto overflow-x-hidden fixed top-0 right-0 left-0 z-50 justify-center items-center w-full md:inset-0 h-[calc(100%-1rem)] max-h-full">
                <div class="relative p-4 w-full max-w-md max-h-full">
                    <div class="relative bg-white rounded-lg shadow dark:bg-gray-700">
                        <div class="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600">
                            <h3 class="text-xl font-semibold text-gray-900 dark:text-white">
                                Update Tag - <%= tag.getName() %>
                            </h3>
                            <button type="button" class="text-gray-400" data-modal-hide="update-tag-modal-<%= tag.getId() %>">
                                <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                                    <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                                </svg>
                                <span class="sr-only">Close modal</span>
                            </button>
                        </div>
                        <div class="p-4 md:p-5">
                            <form action="tags" method="POST" class="space-y-4">
                                <input type="hidden" name="id" value="<%= tag.getId() %>">
                                <input type="hidden" name="_method" value="PUT">
                                <div>
                                    <label for="name-<%= tag.getId() %>" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Name:</label>
                                    <input type="text" id="name-<%= tag.getId() %>" name="name" value="<%= tag.getName() %>" required class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white"/>
                                </div>
                                <button type="submit" class="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">>Update Tag</button>
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
</div>
<!-- Main modal -->
<div id="add-tag-modal" tabindex="-1" class="fixed top-0 left-0 right-0 z-50 hidden w-full p-4 overflow-x-hidden overflow-y-auto">
    <div class="relative w-full max-w-md max-h-full">
        <div class="relative bg-white rounded-lg shadow">
            <button type="button" class="absolute top-3 right-2.5" data-modal-hide="add-tag-modal">✖</button>
            <div class="p-6">
                <h3 class="mb-4 text-xl font-medium">Ajouter Tâche</h3>
                <form action="tags" method="post">
                    <div class="mb-4">
                        <label for="name" class="block text-sm font-medium">Name</label>
                        <input type="text" id="name" name="name" required class="w-full px-3 py-2 border rounded-lg">
                    </div>
                    <button type="submit" class="text-white bg-blue-600 hover:bg-blue-700 px-5 py-2.5 rounded-lg">Ajouter</button>
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/layout/footer.jsp" /><script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>

</body>
</html>

