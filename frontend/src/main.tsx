// src/main.tsx
import React from "react";
import ReactDOM from "react-dom/client";
import {
  createBrowserRouter,
  RouterProvider,
  Link,
  Outlet,
} from "react-router-dom";
import Home from "./pages/Home";
import ProductDetail from "./pages/ProductDetail";
import CartPage from "./pages/CartPage";      // ← 추가
import "./index.css";
import { CartProvider } from "./context/CartContext";

function Layout() {
  return (
    <div style={{ fontFamily: "system-ui, -apple-system, Segoe UI, Roboto, sans-serif" }}>
      <header
        style={{
          padding: "16px 20px",
          borderBottom: "1px solid #eee",
          display: "flex",
          alignItems: "center",
          gap: 16,
          justifyContent: "space-between",
        }}
      >
        <Link to="/" style={{ textDecoration: "none", color: "inherit" }}>
          <h1 style={{ margin: 0, fontSize: 20 }}>🛋️ Furniture Shop</h1>
        </Link>

        {/* 상단 네비에 장바구니 링크 추가 */}
        <nav style={{ display: "flex", gap: 12 }}>
          <Link to="/cart" style={{ textDecoration: "none" }}>
            장바구니
          </Link>
        </nav>
      </header>

      <main style={{ padding: 20 }}>
        <Outlet />
      </main>
    </div>
  );
}

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { index: true, element: <Home /> },
      { path: "products/:id", element: <ProductDetail /> },
      { path: "cart", element: <CartPage /> }, // ← 장바구니 라우트 추가
    ],
  },
  // 404 Fallback (선택)
  { path: "*", element: <div>페이지를 찾을 수 없습니다.</div> },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    {/* 전체 앱을 CartProvider로 감싸서 useCart 사용 가능 */}
    <CartProvider>
      <RouterProvider router={router} />
    </CartProvider>
  </React.StrictMode>
);
