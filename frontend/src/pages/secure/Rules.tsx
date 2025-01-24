import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form } from 'react-bootstrap';
import LoadingSpinner from '../../common/loadingSpinner';

interface Product {
  id: number;
  name: string;
  description: string;
}

interface Rule {
  id: number;
  name: string;
  type: 'Masterproduct' | 'Product';
  condition: 'greater' | 'less' | 'greater_equal' | 'less_equal';
  value: number;
  isActive: boolean;
  product?: Product;
}

const RulesPage: React.FC = () => {
  const [rules, setRules] = useState<Rule[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [showAddRule, setShowAddRule] = useState(false);
  const [selectedRule, setSelectedRule] = useState<Rule | null>(null);
  const [newRule, setNewRule] = useState<Rule>({
    id: 0,
    name: '',
    type: 'Masterproduct',
    condition: 'greater',
    value: 0,
    isActive: true,
  });
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);

  const hardcodedProducts: Product[] = [
    { id: 1, name: 'Masterproduct A', description: 'Masterproduct A description' },
    { id: 2, name: 'Product B', description: 'Product B description' },
    { id: 3, name: 'Masterproduct C', description: 'Masterproduct C description' },
    { id: 4, name: 'Product D', description: 'Product D description' },
  ];

  const hardcodedRules: Rule[] = [
    { 
      id: 1, 
      name: 'Rule #1', 
      type: 'Masterproduct', 
      condition: 'greater', 
      value: 100, 
      isActive: true, 
      product: { id: 1, name: 'Masterproduct A', description: 'Masterproduct A description' }
    },
    { 
      id: 2, 
      name: 'Rule #2', 
      type: 'Product', 
      condition: 'less', 
      value: 50, 
      isActive: false, 
      product: { id: 2, name: 'Product B', description: 'Product B description' }
    },
    { 
      id: 3, 
      name: 'Rule #3', 
      type: 'Masterproduct', 
      condition: 'greater_equal', 
      value: 200, 
      isActive: true, 
      product: { id: 3, name: 'Masterproduct C', description: 'Masterproduct C description' }
    },
    { 
      id: 4, 
      name: 'Rule #4', 
      type: 'Product', 
      condition: 'less_equal', 
      value: 75, 
      isActive: false, 
    },
  ];

  useEffect(() => {
    setIsLoading(true);
    setTimeout(() => {
      setRules(hardcodedRules);
      setIsLoading(false);
    }, 1000);
  }, []);

  const handleToggleActive = (ruleId: number) => {
    setRules(prevRules =>
      prevRules.map(rule =>
        rule.id === ruleId ? { ...rule, isActive: !rule.isActive } : rule
      )
    );
  };

  const handleEdit = (ruleId: number) => {
    const ruleToEdit = rules.find(rule => rule.id === ruleId);
    if (ruleToEdit) {
      setSelectedRule(ruleToEdit);
      setShowAddRule(true);
      filterProducts(ruleToEdit.type); // Filtrar productos al editar
    }
  };

  const handleSaveEditedRule = () => {
    if (selectedRule) {
      console.log('Saving edited rule:', selectedRule);
      setShowAddRule(false);
    }
  };

  const handleAddNewRule = () => {
    const newRuleWithId = { ...newRule, id: rules.length + 1 };
    setRules([...rules, newRuleWithId]);
    console.log('Adding new rule:', newRuleWithId);
    setShowAddRule(false);
    setNewRule({
      id: 0,
      name: '',
      type: 'Masterproduct',
      condition: 'greater',
      value: 0,
      isActive: true,
    });
  };

  const filterProducts = (type: 'Masterproduct' | 'Product') => {
    const filtered = hardcodedProducts.filter(product =>
      type === 'Masterproduct' ? product.name.includes('Masterproduct') : product.name.includes('Product')
    );
    setFilteredProducts(filtered);
  };

  return (
    <div>
      <h1>Rules</h1>

      <div className="text-end mb-3">
        <Button variant="outline-primary" onClick={() => setShowAddRule(true)}>
          Add Rule
        </Button>
      </div>

      {isLoading ? (
        <LoadingSpinner />
      ) : rules.length === 0 ? (
        <p>No rules available</p>
      ) : (
        <Table striped>
          <thead>
            <tr>
              <th>#</th>
              <th>Name</th>
              <th>Type</th>
              <th>Condition</th>
              <th>Value</th>
              <th>Product Name</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {rules.map(rule => (
              <tr key={rule.id}>
                <td>{`#${rule.id.toString().padStart(4, '0')}`}</td>
                <td>{rule.name}</td>
                <td>{rule.type}</td>
                <td>{rule.condition.replace('_', ' ')}</td>
                <td>{rule.value}</td>
                <td>{rule.product ? rule.product.name : 'No product available'}</td>
                <td>
                  <div className="d-flex align-items-center">
                    <Button 
                      variant="outline-primary" 
                      onClick={() => handleEdit(rule.id)} 
                      className="me-2"
                    >
                      <i className="bi bi-pencil-square"></i>
                    </Button>
                    <Form.Switch 
                      label=""
                      checked={rule.isActive}
                      onChange={() => handleToggleActive(rule.id)}
                      id={`switch-${rule.id}`}
                    />
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      {/* Modal para agregar o editar regla */}
      <Modal show={showAddRule} onHide={() => setShowAddRule(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{selectedRule ? 'Edit Rule' : 'Add New Rule'}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Name</Form.Label>
              <Form.Control 
                type="text" 
                value={selectedRule ? selectedRule.name : newRule.name} 
                onChange={(e) => selectedRule 
                  ? setSelectedRule({ ...selectedRule, name: e.target.value }) 
                  : setNewRule({ ...newRule, name: e.target.value })
                }
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Type</Form.Label>
              <Form.Control 
                as="select"
                value={selectedRule ? selectedRule.type : newRule.type} 
                onChange={(e) => {
                  const value = e.target.value as 'Masterproduct' | 'Product';
                  selectedRule 
                    ? setSelectedRule({ ...selectedRule, type: value })
                    : setNewRule({ ...newRule, type: value });
                  filterProducts(value); // Filtrar productos al cambiar el tipo
                }}
              >
                <option value="Masterproduct">Masterproduct</option>
                <option value="Product">Product</option>
              </Form.Control>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Condition</Form.Label>
              <Form.Control 
                as="select"
                value={selectedRule ? selectedRule.condition : newRule.condition} 
                onChange={(e) => {
                  const value = e.target.value as 'greater' | 'less' | 'greater_equal' | 'less_equal';
                  selectedRule 
                    ? setSelectedRule({ ...selectedRule, condition: value })
                    : setNewRule({ ...newRule, condition: value });
                }}
              >
                <option value="greater">Greater</option>
                <option value="less">Less</option>
                <option value="greater_equal">Greater or Equal</option>
                <option value="less_equal">Less or Equal</option>
              </Form.Control>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Value</Form.Label>
              <Form.Control 
                type="number" 
                value={selectedRule ? selectedRule.value : newRule.value} 
                onChange={(e) => selectedRule 
                  ? setSelectedRule({ ...selectedRule, value: +e.target.value })
                  : setNewRule({ ...newRule, value: +e.target.value })
                }
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Product</Form.Label>
              <Form.Control 
                as="select"
                value={selectedRule && selectedRule.product ? selectedRule.product.id : newRule.product?.id || 0}
                onChange={(e) => {
                  const selectedProduct = hardcodedProducts.find(product => product.id === +e.target.value);
                  selectedRule 
                    ? setSelectedRule({ ...selectedRule, product: selectedProduct || undefined }) 
                    : setNewRule({ ...newRule, product: selectedProduct || undefined });
                }}
              >
                <option value="">Select Product</option>
                {filteredProducts.map(product => (
                  <option key={product.id} value={product.id}>{product.name}</option>
                ))}
              </Form.Control>
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowAddRule(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={selectedRule ? handleSaveEditedRule : handleAddNewRule}>
            {selectedRule ? 'Save Rule' : 'Add Rule'}
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default RulesPage;