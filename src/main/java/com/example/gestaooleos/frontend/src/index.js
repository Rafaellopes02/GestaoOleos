import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login/Login';
import CriarConta from './CriarConta/CriarConta';
import Home from './Home/Home';
import Contratos from './Contratos/Contratos';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <Router>
        <Routes>
            <Route path="/Login" element={<Login />} />
            <Route path="/criar-conta" element={<CriarConta />} />
            <Route path="/home" element={<Home />} />
            <Route path="/contratos" element={<Contratos />} />
        </Routes>
    </Router>
);
