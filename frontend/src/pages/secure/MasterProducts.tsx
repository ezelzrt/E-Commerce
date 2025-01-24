import React, { useEffect, useState } from 'react';
import { Table } from 'react-bootstrap';
import { getMasterProducts } from '../../services/masterproductService';
import { apiRequestHandler } from '../../utils/apiRequestHandler';
import LoadingSpinner from '../../common/loadingSpinner';
import { MasterProduct } from '../../common/appTypes';

const Masterproducts: React.FC = () => {
    const [masterProducts, setMasterProducts] = useState<MasterProduct[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        setIsLoading(true);
        apiRequestHandler(getMasterProducts, setMasterProducts)
            .finally(() => setIsLoading(false));
    }, []);

    return (
        <div>
            <h1>Masterproducts</h1>
            {isLoading ? (
                <LoadingSpinner />
            ) : masterProducts.length === 0 ? (
                <p>No master products available</p>
            ) : (
                <Table striped>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th># Related products</th>
                            <th>Attributes</th>
                            <th>Base price</th>
                        </tr>
                    </thead>
                    <tbody>
                        {masterProducts.map((masterProduct) => (
                            <tr key={masterProduct.id}>
                                <td>{masterProduct.id}</td>
                                <td>{masterProduct.name}</td>
                                <td>{masterProduct.products.length}</td>
                                <td>{masterProduct.attributes.map((e) => e.name).join(', ')}</td>
                                <td>${masterProduct.basePrice.toFixed(2)}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </div>
    );
    
};

export default Masterproducts;
