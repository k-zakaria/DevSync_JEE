<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.capps.entity.User" %> <!-- Change le chemin selon le bon package -->
<%@ page import="org.capps.entity.UserRole" %>

<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.css" rel="stylesheet" />
</head>
<body>
<jsp:include page="/layout/navbar.jsp" />

<section class="bg-gray-50 dark:bg-gray-900 h-100 py-8 ">
    <div class="flex flex-col items-center justify-center px-6 py-4 mx-auto md:h-auto lg:py-4">
        <div class="w-full max-w-lg bg-white rounded-lg shadow dark:border dark:bg-gray-800 dark:border-gray-700">
            <div class="p-6 space-y-4 sm:p-8">
                <h1 class="text-xl font-bold leading-tight text-gray-900 md:text-2xl dark:text-white">
                    Sign in to your account
                </h1>
                <form action="login" method="POST" class="space-y-4">
                    <div>
                        <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Your email</label>
                        <input type="email" name="email" id="email" class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white" required>
                    </div>
                    <div>
                        <label for="password" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
                        <input type="password" name="password" id="password" class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white" required>
                    </div>
                    <button type="submit" class="w-full text-white bg-blue-600 hover:bg-blue-700 rounded-lg px-5 py-2.5">Sign in</button>
                </form>
            </div>
        </div>
    </div>
</section>



</body>
</html>

