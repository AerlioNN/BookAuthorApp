import { createRouter, createWebHistory } from "vue-router";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Home from "../views/Home.vue";
import BookManagement from "../views/BookManagement.vue";
import AuthorsManagement from "../views/AuthorsManagement.vue";

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
    meta: { requiresAuth: true },
  },
  {
      path: "/books",
      name: "BookManagement",
      component: BookManagement,
      meta: { requiresAuth: true },
    },
    {
        path: "/authors",
        name: "AuthorsManagement",
        component: AuthorsManagement,
        meta: { requiresAuth: true },
      },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

// Check authentication
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = localStorage.getItem('token');
    if (!token) {
      next({ name: 'Login' });
    } else {
      next();
    }
  } else {
    // Allow access to the protected route
    next();
  }
});

export default router;
