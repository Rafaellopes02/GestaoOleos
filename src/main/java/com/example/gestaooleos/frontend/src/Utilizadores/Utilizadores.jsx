import React, { useEffect, useState } from 'react';
import axios from 'axios';
import SideBar from '../Components/Sidebar/sidebar';
import './Utilizadores.css';

import ReactVirtualizedTable from './table.jsx';
import { FiHome } from "react-icons/fi";
import { GrGroup } from "react-icons/gr";
import { RiGroupLine } from "react-icons/ri";

function Utilizadores() {
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
                    <h1>Utilizadores</h1>
                </div>
                    <div className="cards-row">
                        <div className="card">
                            <p><b>Escritórios</b></p>
                            <div className="content">
                                <span className="count"><b>{contagens["Escritórios"] ?? 0}</b></span>
                                <span className="icon-wrapper yellow"><FiHome /></span>
                            </div>
                        </div>

                        <div className="card">
                            <p><b>Clientes</b></p>
                            <div className="content">
                                <span className="count"><b>{contagens["Clientes"] ?? 0}</b></span>
                                <span className="icon-wrapper green"><GrGroup /></span>
                            </div>
                        </div>

                        <div className="card">
                            <p><b>Comerciais</b></p>
                            <div className="content">
                                <span className="count"><b>{contagens["Comerciais"] ?? 0}</b></span>
                                <span className="icon-wrapper red"><RiGroupLine /></span>
                            </div>
                        </div>

                        <div className="card">
                            <p><b>Empregados</b></p>
                            <div className="content">
                                <span className="count"><b>{contagens["Empregados"] ?? 0}</b></span>
                                <span className="icon-wrapper orange"><RiGroupLine /></span>
                            </div>
                        </div>
                    </div>

                <ReactVirtualizedTable />
            </div>
        </div>
    );
}

export default Utilizadores;
