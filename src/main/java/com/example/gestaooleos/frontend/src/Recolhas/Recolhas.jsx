import React, { useEffect, useState } from 'react';
import axios from 'axios';
import SideBar from '../Components/Sidebar/sidebar';
import './Recolhas.css';

import { FaCirclePlus } from "react-icons/fa6";

import ReactVirtualizedTable from './table.jsx';
import { jwtDecode } from "jwt-decode";

function Recolhas() {
    const [contratos, setContratos] = useState([]);
    const [contratoSelecionado, setContratoSelecionado] = useState("");
    const [morada, setMorada] = useState("");
    const [codigoPostal, setCodigoPostal] = useState("");
    const [dataRecolha, setDataRecolha] = useState("");
    const [horaRecolha, setHoraRecolha] = useState("");
    const [observacoes, setObservacoes] = useState("");


    useEffect(() => {
        const fetchContratos = async () => {
            try {
                const token = localStorage.getItem("token");
                const decoded = jwtDecode(token);
                const tipo = parseInt(decoded.tipo, 10);
                const idUtilizador = parseInt(decoded.sub, 10);

                const response = await axios.get('http://localhost:8080/Contratos', {
                    headers: { Authorization: `Bearer ${token}` }
                });

                let contratosFiltrados = response.data;

                if (tipo === 1) {
                    contratosFiltrados = contratosFiltrados.filter(c =>
                        c.idutilizador === idUtilizador && c.idestadocontrato === 1
                    );
                }

                setContratos(contratosFiltrados);
            } catch (error) {
                console.error("Erro ao buscar contratos:", error);
            }
        };

        fetchContratos();
    }, []);

    const handleSolicitarRecolha = async () => {
        try {
            const token = localStorage.getItem("token");
            const decoded = jwtDecode(token);
            const idUtilizador = parseInt(decoded.sub, 10);

            const novaRecolha = {
                idcontrato: contratoSelecionado,
                idutilizador: idUtilizador,
                quantidade: 0,
                numbidoes: 0,
                morada,
                codigopostal: codigoPostal,
                data: `${dataRecolha}T${horaRecolha}`,
                observacoes,
                idestadorecolha: 1
            };

            console.log("Recolha a enviar:", novaRecolha);

            await axios.post("http://localhost:8080/Recolhas", novaRecolha);
            alert("Recolha solicitada com sucesso!");
        } catch (error) {
            console.error("Erro ao solicitar recolha:", error);
            alert("Erro ao solicitar recolha!");
        }
    };

    return (
        <div>
            <SideBar />
            <div className="main-content">
                <div className="header">
                    <h1>Recolhas</h1>
                </div>

                <div className="cards-row">
                    <h2>Solicitar Recolha de Óleo</h2>
                    <div className="form-recolha">
                        <div className="linha-form">
                            <select className="SelectContrato" value={contratoSelecionado} onChange={e => setContratoSelecionado(e.target.value)}>
                                <option value="">Selecione o Contrato...</option>
                                {contratos.map(c => (
                                    <option key={c.idcontrato} value={c.idcontrato}>
                                        {c.nome}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="linha-form">
                            <input type="text" placeholder="Morada da Recolha" value={morada} onChange={e => setMorada(e.target.value)} />
                            <input type="text" placeholder="Código-Postal" value={codigoPostal} onChange={e => setCodigoPostal(e.target.value)} />
                        </div>

                        <div className="linha-form">
                            <input type="date" value={dataRecolha} onChange={e => setDataRecolha(e.target.value)} />
                            <input type="time" value={horaRecolha} onChange={e => setHoraRecolha(e.target.value)} />
                        </div>

                        <div className="linha-form">
                            <textarea className="textarea-observacoes" placeholder="Observações (Opcional)" value={observacoes} onChange={e => setObservacoes(e.target.value)} />
                        </div>

                        <div className="linha-form">
                            <button className="botao-solicitar" onClick={handleSolicitarRecolha}>
                                Solicitar Recolha <FaCirclePlus />
                            </button>
                        </div>
                    </div>
                </div>

                <ReactVirtualizedTable />
            </div>
        </div>
    );
}

export default Recolhas;
