import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './Login/Login';
import CriarConta from './CriarConta/CriarConta';
import Home from './Home/Home';
import Contratos from './Contratos/Contratos';
import Recolhas from './Recolhas/Recolhas';
import Utilizadores from './Utilizadores/Utilizadores';
import Pagamentos from './Pagamentos/Pagamentos';
import PrivateRoute from './Components/PrivateRoute';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
    <Router>
        <Routes>
            <Route path="/" element={<Navigate to="/Login" />} />
            <Route path="/Login" element={<Login />} />
            <Route path="/criar-conta" element={<CriarConta />} />

            <Route path="/home" element={
                <PrivateRoute>
                    <Home />
                </PrivateRoute>
            } />

            <Route path="/contratos" element={
                <PrivateRoute>
                    <Contratos />
                </PrivateRoute>
            } />

            <Route path="/recolhas" element={
                <PrivateRoute>
                    <Recolhas />
                </PrivateRoute>
            } />

            <Route path="/pagamentos" element={
                <PrivateRoute>
                    <Pagamentos />
                </PrivateRoute>
            } />

            <Route path="/utilizadores" element={
                <PrivateRoute>
                    <Utilizadores />
                </PrivateRoute>
            } />
        </Routes>
    </Router>
);
