import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './sidebar.css';
import {CreditCard, FileText, Home, LogOut, Truck, Users} from "lucide-react";

function Sidebar() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/Login');
    };

    return (
        <div className="sidebar">
            <div className="image">
                <img src="/images/img.png" alt="Logo" className="logo" />
                <img src="/images/letras.png" alt="Logo" className="logo" />
            </div>
            <nav>
                <Link to="/Home"><Home size={18} /> Página Inicial</Link>
                <Link to="/Contratos"><FileText size={18} /> Contratos</Link>
                <Link to="/Recolhas"><Truck size={18} /> Recolhas</Link>
                <Link to="/Pagamentos"><CreditCard size={18} /> Pagamentos</Link>
                <button onClick={handleLogout}>
                    <LogOut size={18} /> Logout
                </button>
            </nav>
        </div>
    );
}

export default Sidebar;
