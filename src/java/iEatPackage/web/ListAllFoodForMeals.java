/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iEatPackage.web;

import iEatPackage.model.Food;
import iEatPackage.model.User;
import iEatPackage.model.UserDao;
import iEatPackage.model.UserDaoInMemoryImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ola
 */
public class ListAllFoodForMeals extends HttpServlet {

    private UserDao userDao = UserDaoInMemoryImpl.instance();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            List<Food> listOfAllFood = userDao.getAllFoods();
            request.setAttribute("listOfAllFood", listOfAllFood);

            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    // ToDo
                    response.sendRedirect("login.jsp");
                } else if ((user.getUsertype().equals(("Admin").toLowerCase()))) {

                    RequestDispatcher view = request.getRequestDispatcher("ListAllFoodServlet.do");
                    view.forward(request, response);
                } else if (!userDataCompleted(user) && (user.getUsertype().equals(("Basic").toLowerCase()))) {
                    //out.println("form");
                    RequestDispatcher view = request.getRequestDispatcher("userdataform.jsp");
                    view.forward(request, response);
                } else {
                    request.setAttribute("listOfAllFood", listOfAllFood);
                    RequestDispatcher view = request.getRequestDispatcher("createnewmeal.jsp");
                    view.forward(request, response);
                }
            } else {
                response.sendRedirect("login.jsp");
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public boolean userDataCompleted(User user) {

        if (user.getHeight() == 0
                || user.getWeight() == 0) {
            return false;
        }
        return true;
    }

}
