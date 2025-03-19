import React from "react";
import { Routes, Route } from "react-router-dom";
import InvoiceApp from "./invoice";


function App() {
  return (
    <Routes>
     <Route path="/invoice" element={<InvoiceApp />} />


    </Routes>
  );
}

export default App;
