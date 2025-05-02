import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login/Login';
import CriarConta from './CriarConta/CriarConta';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <Router>
        <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/criar-conta" element={<CriarConta />} />
        </Routes>
    </Router>
);
