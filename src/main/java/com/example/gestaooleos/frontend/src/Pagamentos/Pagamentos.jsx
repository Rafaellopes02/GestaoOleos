import React, { useEffect, useState } from 'react';
import axios from 'axios';
import SideBar from '../Components/Sidebar/sidebar';
import './Pagamentos.css';

import ReactVirtualizedTable from './table.jsx';


function Pagamentos() {
    const [contagens, setContagens] = useState({});

    useEffect(() => {
        const fetchContagens = async () => {
            try {
                const response = await axios.get('http://localhost:8080/Utilizadores/contar-por-tipo');
                setContagens(response.data);
            } catch (error) {
                console.error("Erro ao buscar contagens:", error);
            }
        };
        fetchContagens();
    }, []);

    return (
        <div>
            <SideBar />
            <div className="main-content">
                <div className="header">
                    <h1>Pagamentos</h1>
                </div>

                <ReactVirtualizedTable />
            </div>
        </div>
    );
}

export default Pagamentos;
