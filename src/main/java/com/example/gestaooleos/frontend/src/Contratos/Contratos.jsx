import React, { useEffect, useState } from 'react';
import axios from 'axios';
import SideBar from '../Components/Sidebar/sidebar';
import NovoContrato from './NovoContrato';
import './Contratos.css';

import { FaRegNewspaper } from "react-icons/fa";
import { FaCirclePlus } from "react-icons/fa6";

import ReactVirtualizedTable from './table.jsx';
import { jwtDecode } from "jwt-decode";

function Contratos() {
    const [contagens, setContagens] = useState({});
    const [modalOpen, setModalOpen] = useState(false);

    useEffect(() => {
        const fetchContagens = async () => {
            try {
                const token = localStorage.getItem("token");
                if (!token) return;

                const decoded = jwtDecode(token);
                const tipo = parseInt(decoded.tipo, 10);
                const idUtilizador = parseInt(decoded.sub, 10);

                let response;
                if (tipo === 1) {
                    response = await axios.get(`http://localhost:8080/Contratos/contar-estados/utilizador/${idUtilizador}`);
                } else {
                    response = await axios.get('http://localhost:8080/Contratos/contar-estados');
                }

                setContagens(response.data);
            } catch (error) {
                console.error("Erro ao buscar contagens:", error);
            }
        };

        fetchContagens();
    }, []);


    const handleSaveContrato = async (form) => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("Utilizador não autenticado.");
                return;
            }

            const decoded = jwtDecode(token);
            console.log("Token decodificado:", decoded);

            const idUtilizador = parseInt(decoded.sub, 10);

            const novoContrato = {
                nome: form.nome,
                dataInicio: form.dataInicio,
                dataFim: form.dataFim,
                idutilizador: idUtilizador,
                valor: 0,
                idEstadoContrato: 1,
            };


            await axios.post("http://localhost:8080/Contratos", novoContrato);
            alert("Contrato criado com sucesso!");
            setModalOpen(false);
        } catch (error) {
            console.error("Erro ao guardar contrato:", error);
            alert("Erro ao guardar contrato!");
        }
    };

    return (
        <div>
            <SideBar />
            <div className="main-content">
                <div className="header">
                    <h1>Contratos</h1>
                </div>

                <div className="cards-row">
                    <div className="card">
                        <p><b>Contratos Ativos</b></p>
                        <div className="content">
                            <span className="count"><b>{contagens["ativos"] ?? 0}</b></span>
                            <span className="icon-wrapper green"><FaRegNewspaper /></span>
                        </div>
                    </div>

                    <div className="card">
                        <p><b>Contratos Concluídos</b></p>
                        <div className="content">
                            <span className="count"><b>{contagens["concluidos"] ?? 0}</b></span>
                            <span className="icon-wrapper red"><FaRegNewspaper /></span>
                        </div>
                    </div>

                    <div className="buttonAddContrato" onClick={() => setModalOpen(true)}>
                        <div className="content">
                            <span className="icon-wrapper yellow"><FaCirclePlus /></span>
                        </div>
                        <p><b>Adicionar Contrato</b></p>
                    </div>
                </div>

                <ReactVirtualizedTable />
                <NovoContrato
                    open={modalOpen}
                    onClose={() => setModalOpen(false)}
                    onSave={handleSaveContrato}
                />
            </div>
        </div>
    );
}

export default Contratos;
