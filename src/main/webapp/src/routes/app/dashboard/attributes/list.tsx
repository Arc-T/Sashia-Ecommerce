import { useState } from "react";
import { useNavigate } from "react-router";
import { useTranslation } from "react-i18next";
import { toast } from "sonner";
import { Download, FileUp, List, Plus, Settings2 } from "lucide-react";

import PageHeader from "@/components/custom/page-header";
import DataTable from "@/components/custom/datatable/table";

import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import { useGetAttributes } from "@/hooks/use-attributes";
import { UNPAGED } from "@/types/page";
import type { AttributeFilter } from "@/types/attribute";

import { attributeListColumn } from "./-column";
import AttributeSearch from "./-search";

export function AttributesList() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const [filter, setFilter] = useState<Partial<AttributeFilter>>(UNPAGED);

  const query = useGetAttributes(filter);

  return (
    <div className="space-y-4">
      <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-start">
        <PageHeader title={t("attributes_list")} icon={<List />} />

        <div className="flex flex-wrap items-center gap-2">
          <Button
            className="h-8 gap-2"
            onClick={() => navigate("/dashboard/attributes/create")}
          >
            <Plus className="h-4 w-4" />

            <span className="hidden sm:inline">{t("add_attribute")}</span>

            <span className="sm:hidden">{t("add")}</span>
          </Button>

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="outline" size="icon" className="h-9 w-9">
                <Settings2 className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>

            <DropdownMenuContent align="end" className="w-48">
              <DropdownMenuItem
                className="gap-2"
                onClick={() => toast.info(t("Exporting data..."))}
              >
                <Download className="h-4 w-4" />
                {t("Export")}
              </DropdownMenuItem>

              <DropdownMenuSeparator />

              <DropdownMenuItem className="gap-2 text-destructive">
                <FileUp className="h-4 w-4" />
                {t("Import")}
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>

      <DataTable
        query={query}
        columns={attributeListColumn}
        pageSize={filter.size}
        onPaginationClick={setFilter}
        onRowClick={(id) => navigate(`/dashboard/attributes/${id}/show`)}
        searchComponent={
          <AttributeSearch
            onSearch={setFilter}
            onClear={() => setFilter(UNPAGED)}
          />
        }
      />
    </div>
  );
}
