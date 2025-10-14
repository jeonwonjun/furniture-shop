import { useState } from "react";
import reactLogo from "./assets/react.svg";
import viteLogo from "/vite.svg";
import "./App.css";

import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import { CartProvider } from "./context/CartContext";
import AddToCartButton from "./components/AddToCartButton";
import CartPage from "./pages/CartPage";

function Home() {
  const [count, setCount] = useState(0);

  // 데모용 productId=1 버튼 추가
  return (
    <>
      <div>
        <a href="https://vite.dev" target="_blank" rel="noreferrer">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank" rel="noreferrer">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>
      <h1>Vite + React</h1>
      <div className="card">
        <button onClick={() => setCount((c) => c + 1)}>count is {count}</button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>

      {/* 장바구니 데모 섹션 */}
      <div className="card" style={{ marginTop: 24 }}>
        <h2 style={{ marginBottom: 8 }}>상품 상세 데모</h2>
        <p style={{ marginBottom: 12 }}>productId=1 을 장바구니에 담아보세요.</p>
        <AddToCartButton productId={1} />
        <div style={{ marginTop: 12 }}>
          <Link to="/cart">장바구니로 이동 →</Link>
        </div>
      </div>

      <p className="read-the-docs">Click on the Vite and React logos to learn more</p>
    </>
  );
}

function App() {
  return (
    <CartProvider>
      <BrowserRouter>
        <div style={{ padding: 16 }}>
          {/* 간단한 상단 네비게이션 */}
          <nav style={{ marginBottom: 16, display: "flex", gap: 12 }}>
            <Link to="/">홈</Link>
            <Link to="/cart">장바구니</Link>
          </nav>

          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/cart" element={<CartPage />} />
          </Routes>
        </div>
      </BrowserRouter>
    </CartProvider>
  );
}

export default App;
